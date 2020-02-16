package akka.persistence.datastore

import akka.actor.typed.{ActorSystem, Props}
import akka.persistence.PersistentActor
import akka.persistence.query.PersistenceQuery
import org.scalatest._

import scala.io.StdIn
import akka.persistence.datastore.MyPersistentBehavior.Increment
import org.scalatest.flatspec.AnyFlatSpec

class ExampleApp extends AnyFlatSpec {

  "" should "" in {
    val behavior = MyPersistentBehavior.apply() 
    val actor = ActorSystem.create(behavior, "test")
    actor.ref.tell(Increment)
  }
}
