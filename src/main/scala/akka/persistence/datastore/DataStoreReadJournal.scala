package akka.persistence.datastore

import akka.actor.ExtendedActorSystem
import akka.persistence.query.Offset
import akka.persistence.query.scaladsl._
import com.typesafe.config.Config

object DataStoreReadJournal {
  final val Identifier = "datastore-journal.query"
}

class DataStoreReadJournal(as: ExtendedActorSystem, config: Config) extends ReadJournal
  with EventsByPersistenceIdQuery
  with CurrentEventsByPersistenceIdQuery
  with EventsByTagQuery
  with CurrentEventsByTagQuery
  with CurrentPersistenceIdsQuery
  with PersistenceIdsQuery {


  override def eventsByPersistenceId(persistenceId: String, fromSequenceNr: Long, toSequenceNr: Long) = {
   ???
  }
  override def currentEventsByPersistenceId(persistenceId: String, fromSequenceNr: Long, toSequenceNr: Long) = ???
  override def eventsByTag(tag: String, offset: Offset) = ???
  override def currentEventsByTag(tag: String, offset: Offset) = ???
  override def currentPersistenceIds() = ???
  override def persistenceIds() = ???
}
