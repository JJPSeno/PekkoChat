package actions

import javax.inject._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}

class SecureAction @Inject() (parser: BodyParsers.Default)
(implicit ec: ExecutionContext)
  extends ActionBuilderImpl(parser){
    override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
      request.session.get("username") match {
        case Some(username) => block(request)
        case None => Future.successful(Results.Unauthorized("Not logged in."))
      }
  }
}
