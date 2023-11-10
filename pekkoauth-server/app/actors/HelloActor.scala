package actors

import org.apache.pekko.actor._

object HelloActor {
  def props = Props[HelloActor]()

  case class SayHello(name: String)
}

class HelloActor extends Actor {
  import HelloActor._

  def receive = {
    case SayHello(message: String) =>
      sender() ! "Hello, " + message
  }
}
