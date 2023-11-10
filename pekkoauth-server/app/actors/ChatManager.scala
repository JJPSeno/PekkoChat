package actors

import org.apache.pekko.actor._

class ChatManager extends Actor{
  import ChatManager._
  private var chatters = List.empty[ActorRef]
  def receive = {
      case NewChatter(chatter) => chatters ::= chatter
      case Message(msg, chatter) => for (c <- chatters) if (c != chatter) c ! ChatActor.SendMessage(msg)
      case m => println("Unhandled message in ChatManager: "+m)
  }
}

object ChatManager{
  case class NewChatter(chatter: ActorRef)
  case class Message(msg: String, chatter: ActorRef)
}
