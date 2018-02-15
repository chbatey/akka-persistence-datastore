package akka.persistence.datastore

import akka.actor.{ActorSystem, Props}
import akka.persistence.PersistentActor
import akka.persistence.query.PersistenceQuery

import scala.io.StdIn

class StupidActor(val persistenceId: String) extends PersistentActor {
  override def receiveRecover: Receive = {
    case msg => println("Recovered: " + msg)
  }
  override def receiveCommand: Receive = {
    case cmd => persist(cmd.toString) { e =>
      println("Persisted: " + e)
    }
  }
}

object ExampleApp extends App {
  val system = ActorSystem()

  val sa = system.actorOf(Props[StupidActor](new StupidActor("p102")))

//  (1 to 100).foreach { i =>
//    sa ! s"e-$i"
//  }


  val queries = PersistenceQuery(system).readJournalFor[DataStoreReadJournal](DataStoreReadJournal.Identifier)

  StdIn.readLine()
  system.terminate()
}
