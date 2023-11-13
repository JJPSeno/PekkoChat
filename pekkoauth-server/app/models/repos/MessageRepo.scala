package models.repos

import models.domains._
import javax.inject._
import play.api.db.slick.HasDatabaseConfigProvider
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}
import java.util.UUID

// final case class Message(
//   id: UUID = UUID.randomUUID(),
//   userId: UUID,
//   username: String,
//   message: Option[String],
//   photoType: Option[String],
//   photoBytes: Option[Array[Byte]]
// )

@Singleton
class MessageRepo @Inject() 
(protected val dbConfigProvider: DatabaseConfigProvider, val userRepo: UserRepo)
(implicit executionContext: ExecutionContext) 
extends HasDatabaseConfigProvider[JdbcProfile]{
  import profile.api._

  protected class MessageTable(tag: Tag) extends Table[Message](tag, "MESSAGES"){
    def userId = column[UUID]("USER_ID")
    def username = column[String]("USERNAME", O.Length(255))
    def message = column[Option[String]]("MESSAGE", O.Length(255))
    def photoType = column[Option[String]]("PHOTO_TYPE", O.Length(10))
    def photoBytes = column[Option[Array[Byte]]]("PHOTO_BYTES", O.SqlType("BINARY(MAX)"))
    def id = column[UUID]("ID", O.PrimaryKey)

    def user = foreignKey("USER_FK", userId, userRepo.UserTable)(_.id, onDelete = ForeignKeyAction.Cascade)

    def * = (userId, username, message, photoType, photoBytes, id).mapTo[Message]
  }

  lazy val MessageTable = TableQuery[MessageTable]
  def createMessageSchema = MessageTable.schema.createIfNotExists

  def all() = db.run(MessageTable.result)
  def getMessage(targetMessage: UUID) = db.run(MessageTable.filter(_.id === targetMessage).result.headOption)
  def insert (newMessage: Message) = {
    println("insert hit")
    db.run(MessageTable += newMessage)
  }

}