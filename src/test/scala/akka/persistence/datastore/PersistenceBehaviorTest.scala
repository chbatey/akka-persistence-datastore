package akka.persistence.datastore

import akka.persistence.typed.scaladsl.EventSourcedBehavior
import akka.persistence.typed.PersistenceId
import akka.actor.typed.Behavior
import akka.persistence.datastore.MyPersistentBehavior.Increment
import akka.persistence.typed.scaladsl.Effect
import akka.persistence.datastore.MyPersistentBehavior.Incremented

object MyPersistentBehavior {
  sealed trait Command
  case object Increment extends Command
  sealed trait Event
  case class Incremented(value: Int) extends Event
  final case class State(value: Int)
  

  def apply(): Behavior[Command] =
    EventSourcedBehavior[Command, Event, State](
      persistenceId = PersistenceId.ofUniqueId("my-entity"),
      emptyState = State(0),
      commandHandler = (state, cmd) => { 
        cmd match {
          case Increment => Effect.persist(Incremented(1))
        }
      },
      eventHandler = (state, evt) => {
        evt match {
          case Incremented(value) => state.copy(value = state.value + value)
        }
      }
    )
}
