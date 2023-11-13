package controllers

import javax.inject._
import models.domains._
import models.repos._
import models.services.HomeService
import actors.HelloActor
import actors.HelloActor._
import org.apache.pekko.actor._
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import org.apache.pekko.pattern._
import org.apache.pekko.util.Timeout
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scala.util.Failure
import scala.util.Success
import java.util.UUID
import actors.HelloActor
import actors.ChatActor
import actors.ChatManager
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.stream.Materializer
import play.api.libs.streams.ActorFlow
import actions.SecureAction

final case class Login(
  username: String,
  password: String
)

val loginForm: Form[Login] = Form(mapping(
  "username"-> nonEmptyText, 
  "password" -> nonEmptyText)
  (Login.apply)((loginForm: Login) => Some(loginForm.username, loginForm.password)))
  
final case class Register(
  username: String,
  password: String
)

val registerForm: Form[Register] = Form(mapping(
  "username"-> nonEmptyText, 
  "password" -> nonEmptyText)
  (Register.apply)((registerForm: Register) => Some(registerForm.username, registerForm.password)))

implicit val userWrites: Writes[User] = new Writes[User] {
  def writes(user: User) = Json.obj(
    "id"      -> user.id,
    "username"  -> user.username,
  )
}

implicit val userReads: Reads[User] = (
(JsPath \ "id").read[UUID] and
  (JsPath \ "username").read[String] and
  (JsPath \ "password").read[String]
)(User.apply _)

implicit val messageWrites: Writes[Message] = new Writes[Message] {
  def writes(message: Message) = Json.obj(
    "id" -> message.id,
    "userId"  -> message.userId,
    "username"  -> message.username,
    "message"  -> message.message,
    "photoType"  -> message.photoType,
    "photoBytes"  -> message.photoBytes
  )
}

final case class MessageFromForm(
  userId: UUID,
  username: String,
  message: Option[String] = None,
  photoType: Option[String] = None,
  photoBytes: Option[Array[Byte]] = None)

implicit val messageReads: Reads[MessageFromForm] = (
  (JsPath \ "userId").read[UUID] and
  (JsPath \ "username").read[String] and
  (JsPath \ "message").readNullable[String] and
  (JsPath \ "photoType").readNullable[String] and
  (JsPath \ "photoBytes").readNullable[Array[Byte]]
)(MessageFromForm.apply _)

@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents, val homeService: HomeService, val secureAction: SecureAction) 
(implicit ec: ExecutionContext, system: ActorSystem, mat: Materializer)
extends BaseController {
  implicit val timeout: Timeout = 5.seconds
  val helloActor = system.actorOf(HelloActor.props, "hello-actor")
  val chatManager = system.actorOf(Props[ChatManager](), "chatmanager-actor")

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def init() = Action.async{ implicit request: Request[AnyContent] =>
    //fullPostSvc.createSchemas.map( _ => Redirect(routes.HomeController.login()).withSession("auth" -> authThing))
    homeService.createSchemas.map(_=>Ok)
  }

  def sayHello() = secureAction.async { implicit request =>
    val username = request.session.get("username").getOrElse("Error")
    (helloActor ? SayHello(username)).mapTo[String].map { message => Ok(message) }
  }

  def socket() = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef { out =>
      ChatActor.props(out, chatManager)
    }
  }

  def login() = 
    Action.async { implicit request =>
      loginForm.bindFromRequest().fold(
        formWithErrors => {
          println(formWithErrors)
          Future.successful(BadRequest("Failed to login. Errors: " + formWithErrors.errors))
        },
        login => {
          println(login)
          request.session.get("username") match {
            case Some(username) => 
              Future.successful(Ok("Already logged in as " + username)) 
            case None => 
              homeService.validateUser(login.username).map { 
              case Some(user) =>
                if (login.password == user.password) {
                  println("login success")
                  Ok(Json.toJson(user)).withSession("username" -> user.username)
                } else {
                  Unauthorized("Incorrect username or password")
                }
              case None => Unauthorized("Incorrect username or password")
              }
            }
        }
      )
    }

  def logout() = 
    secureAction.async { implicit request =>
      Future.successful(Ok("Logging out").withNewSession)
    }

  def register() = 
    Action.async { implicit request =>
    registerForm.bindFromRequest().fold(
      formWithErrors => {
        Future.successful(BadRequest("Failed to register. Errors: " + formWithErrors.errors))
      },
      register => {
        homeService.registerUser(register.username, register.password).map(_ => Ok("Successfully Registered"))
        })
    }

  def addMessage() = 
    secureAction.async { implicit request =>
      request.body.asJson match {
        case Some(json) => 
          val message = json.as[MessageFromForm]
          println(message)
          if (message.message == None && message.photoType == None && message.photoBytes == None) {
            Future.successful(BadRequest("Need message or photo"))
          } else {
            homeService.addMessage(message.userId, message.username, message.message, message.photoType, message.photoBytes).map(_ => Ok("Added message"))
          }
        case None => Future.successful(BadRequest("Failed to add message"))
      }
    
    }
  
  def getAllUsers() =
    Action.async { implicit request =>
      homeService.checkUserSchema.map { users => Ok(Json.toJson(users)) }
  }

  def getAllMessages() =
    Action.async { implicit request =>
      homeService.checkMessageSchema.map { messages => Ok(Json.toJson(messages)) }
  }

}


