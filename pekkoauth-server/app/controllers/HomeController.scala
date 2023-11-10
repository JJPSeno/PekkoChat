package controllers

import javax.inject._
import actors.HelloActor
import actors.HelloActor._
import org.apache.pekko.actor._
import play.api._
import play.api.mvc._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import org.apache.pekko.pattern._
import org.apache.pekko.util.Timeout
import play.api.libs.json._
import scala.concurrent.Future
import actors.HelloActor
import actors.ChatActor
import actors.ChatManager
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.stream.Materializer
import play.api.libs.streams.ActorFlow

@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) 
(implicit ec: ExecutionContext, system: ActorSystem, mat: Materializer)
extends BaseController {
  implicit val timeout: Timeout = 5.seconds
  val helloActor = system.actorOf(HelloActor.props, "hello-actor")
  val chatManager = system.actorOf(Props[ChatManager](), "chatmanager-actor")

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def sayHello() = Action.async { implicit request =>
    (helloActor ? SayHello(request.body.asText.getOrElse("Message not found"))).mapTo[String].map { message => Ok(message) }
  }

  def socket() = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef { out =>
      ChatActor.props(out, chatManager)
    }
  }
}
