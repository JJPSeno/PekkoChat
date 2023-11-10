package models.repos

import models.domains._
import javax.inject._
import play.api.db.slick.HasDatabaseConfigProvider
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}
import java.util.UUID

@Singleton
class UserRepo @Inject() 
(protected val dbConfigProvider: DatabaseConfigProvider)
(implicit executionContext: ExecutionContext) 
extends HasDatabaseConfigProvider[JdbcProfile]{
  import profile.api._

  protected class UserTable(tag: Tag) extends Table[User](tag, "USERS"){
    def id = column[UUID]("ID", O.PrimaryKey)
    def username = column[String]("USERNAME", O.Unique, O.Length(50))
    def password = column[String]("PASSWORD", O.Length(255))

    def * = (id, username, password).mapTo[User]
  }

  lazy val UserTable = TableQuery[UserTable]
  def createUserSchema = UserTable.schema.createIfNotExists

  def all() = db.run(UserTable.result)
  def getUser(targetUser: String) = db.run(UserTable.filter(_.username === targetUser).result.headOption)
  def insert(newUser: User) = {
    db.run(UserTable += newUser)
  }
  
}