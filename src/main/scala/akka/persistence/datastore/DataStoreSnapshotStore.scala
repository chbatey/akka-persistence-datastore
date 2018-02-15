package akka.persistence.datastore

import akka.persistence.{SelectedSnapshot, SnapshotMetadata, SnapshotSelectionCriteria}
import akka.persistence.snapshot.SnapshotStore

import scala.concurrent.Future

class DataStoreSnapshotStore extends SnapshotStore {
  def loadAsync(persistenceId: String, criteria: SnapshotSelectionCriteria): Future[Option[SelectedSnapshot]] = ???
  def saveAsync(metadata: SnapshotMetadata, snapshot: Any): Future[Unit] = ???
  def deleteAsync(metadata: SnapshotMetadata): Future[Unit] = ???
  def deleteAsync(persistenceId: String, criteria: SnapshotSelectionCriteria): Future[Unit] = ???
}
