import akka.actor._

object Client extends App {

  implicit val system = ActorSystem("ClientSystem")

  // The local actor
  val localActor = system.actorOf(Props[ClientActor], name = "ClientActor")

  // Start the action
  localActor ! Message("hi")

}

class ClientActor extends Actor {

  // create the remote actor
  val selection = context.actorSelection("akka.tcp://CollectorSystem@127.0.0.1:2552/user/ClientManagerActor")
  var counter = 0

  def receive = {
    case Message(msg) =>
      println(s"LocalActor received message: '$msg'")

      if(msg == "hi")
        selection ! Message("Hello from the LocalActor")

      if (counter < 5) {
        sender ! Message("Hello back to you")
        counter += 1
      }
    case _ =>
      println("LocalActor got something unexpected.")
  }

}
