package actors

import org.apache.pekko.actor._

class ChatActor(out: ActorRef, manager: ActorRef) extends Actor{
  import ChatActor._

  manager ! ChatManager.NewChatter(self)
    def receive = {
      case s: String => manager ! ChatManager.Message(s, self)
      case SendMessage(msg) => out ! msg
      case m => println("Unhandled message in ChatActor: "+m)
  }
}

object ChatActor {
  def props(out: ActorRef, manager: ActorRef) = Props(new ChatActor(out, manager))

  case class RecieveMessage(toPrint: String)
  case class SendMessage(msg: String)
}
