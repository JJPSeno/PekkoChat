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

@Singleton
class HomeService @Inject()(
  protected val dbConfigProvider: DatabaseConfigProvider, 
  val userRepo: UserRepo
)
(implicit executionContext: ExecutionContext)
extends HasDatabaseConfigProvider[JdbcProfile]{
  import profile.api._
  
  def createSchemas ={ 
    db.run(
      userRepo.createUserSchema
    )
  }

  def checkUserSchema = userRepo.all()

  def registerUser(regUsername: String, regPassword: String) = {
    val newUser = User(username = regUsername, password = regPassword)
    userRepo.insert(newUser)
  }

  def validateUser(loginUsername: String) = {
    userRepo.getUser(loginUsername)
  }
}
