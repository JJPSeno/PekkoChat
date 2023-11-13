package models.services

import models.domains._
import models.repos._
import javax.inject._
import play.api.data._
import play.api.data.Forms._
import play.api.db.slick.HasDatabaseConfigProvider
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}
import scala.collection.mutable.ListBuffer
import java.util.UUID

@Singleton
class HomeService @Inject()(
  protected val dbConfigProvider: DatabaseConfigProvider, 
  val userRepo: UserRepo, val messageRepo: MessageRepo
)
(implicit executionContext: ExecutionContext)
extends HasDatabaseConfigProvider[JdbcProfile]{
  import profile.api._
  
  def createSchemas ={ 
    db.run(
      userRepo.createUserSchema andThen
      messageRepo.createMessageSchema
    )
  }

  def checkUserSchema = userRepo.all()

  def checkMessageSchema = messageRepo.all()

  def registerUser(regUsername: String, regPassword: String) = {
    val newUser = User(username = regUsername, password = regPassword)
    userRepo.insert(newUser)
  }

  def validateUser(loginUsername: String) = {
    userRepo.getUser(loginUsername)
  }

  def addMessage(userId: UUID, username: String, message: Option[String], photoType: Option[String], photoBytes: Option[Array[Byte]]) = {
    println("add message hit")
    val newMessage = Message(userId = userId, username = username, message = message, photoType = photoType, photoBytes = photoBytes)
    messageRepo.insert(newMessage)
  }


}
