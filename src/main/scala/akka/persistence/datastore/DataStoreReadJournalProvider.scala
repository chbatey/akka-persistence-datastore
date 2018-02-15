package akka.persistence.datastore

import akka.actor.ExtendedActorSystem
import akka.persistence.query.{ReadJournalProvider, javadsl}
import akka.persistence.query.scaladsl.ReadJournal
import com.typesafe.config.Config

class DataStoreReadJournalProvider(as: ExtendedActorSystem, config: Config) extends ReadJournalProvider {
  override def scaladslReadJournal(): ReadJournal = new DataStoreReadJournal(as, config)
  // FIXME todo
  override def javadslReadJournal(): javadsl.ReadJournal = new javadsl.ReadJournal {
  }
}
