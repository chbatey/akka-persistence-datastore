package akka.persistence.datastore

import java.io.{File, FileInputStream}

import akka.persistence.journal.AsyncWriteJournal
import akka.persistence.{AtomicWrite, PersistentRepr}
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.spotify.asyncdatastoreclient.QueryBuilder.{asc, desc, eq => dsEq}
import com.spotify.asyncdatastoreclient._
import com.typesafe.config.Config

import scala.jdk.CollectionConverters._
import scala.collection.immutable
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}
import play.api.libs.json.Json
import play.api.libs.json.JsValue

class DatastoreJournal(config: Config, configPath: String) extends AsyncWriteJournal {

  //FIXME config
  private val settings = DataStoreSettings(config)

  val pluginConfig: PluginConfig = PluginConfig(config)
  val eventEncoder: EventEncoder[_] = pluginConfig.eventEncoder

  private val dsConfig = DatastoreConfig.builder.requestTimeout(1000)
    .requestRetry(3)
    .project(settings.project)
    // .host(settings.host)
    .credential(GoogleCredential.fromStream(new FileInputStream(new File(settings.credentialsFile)))
      .createScoped(DatastoreConfig.SCOPES)).build

  // FIXME shutdown
  private val datastore = Datastore.create(dsConfig)
  private implicit val ec: ExecutionContext = context.dispatcher

  override def asyncWriteMessages(messages: immutable.Seq[AtomicWrite]): Future[immutable.Seq[Try[Unit]]] = {
    // FIXME do these one at a time rather than all at once to preserve order
    val batches: Seq[Batch] = messages.groupBy(_.persistenceId).values.flatMap { messagesBatch =>
      messagesBatch.map { aw => 
        val batch = new Batch
        aw.payload.foreach { pr =>
          val row =
            QueryBuilder.insert("Event", s"${pr.persistenceId}-${pr.sequenceNr}")
              .value("persistenceId", pr.persistenceId)
              .value("sequenceNr", pr.sequenceNr)
              // FIXME, do tags
              // .value("tags", List("red", "blue").asJava.asInstanceOf[java.util.List[AnyRef]])
              // FIXME store payload, serialisation etc
              .value("payload", eventEncoder.castAndSerialize(pr.payload))
          batch.add(row)
        }
        batch
      }
    }.toSeq

    val writes: immutable.Seq[Future[Try[Unit]]] = batches.map { b =>
      datastore.executeAsync(b).asScala.map(_ => Success(())).recover { case t => Failure(t) }
    }

    Future.sequence(writes)
  }

  override def asyncDeleteMessagesTo(persistenceId: String, toSequenceNr: Long): Future[Unit] = {
    context.system.log.debug("Sure, we deleted them lol")
    Future.successful(())
  }

  override def asyncReplayMessages(persistenceId: String, fromSequenceNr: Long, toSequenceNr: Long, max: Long)(recoveryCallback: PersistentRepr => Unit): Future[Unit] = {
    //select sequenceNr from Event where persistenceId = 'pid2' order by sequenceNr asc
    val query = QueryBuilder.query()
      .kindOf("Event")
      .filterBy(dsEq("persistenceId", persistenceId))
      .orderBy(asc("sequenceNr"))

    // FIXME, paging
    datastore.executeAsync(query).asScala.map { result: QueryResult =>
      val rows = result.getAll
      rows.forEach { e =>
        val pr = PersistentRepr(eventEncoder.deserialize(e.getString("payload")), e.getInteger("sequenceNr"), e.getString("persistenceId"))
        recoveryCallback(pr)
      }
    }
  }

  override def asyncReadHighestSequenceNr(persistenceId: String, fromSequenceNr: Long): Future[Long] = {
    //select sequenceNr from Event where persistenceId = 'pid2' order by sequenceNr desc limit 1
    val query = QueryBuilder.query()
      .kindOf("Event")
      .filterBy(dsEq("persistenceId", persistenceId))
      .orderBy(desc("sequenceNr"))
      .limit(1)

    datastore.executeAsync(query).asScala.map { result =>
      val all = result.getAll.asScala
      all.size match {
        case 0 => 0L
        case 1 => all.head.getInteger("sequenceNr")
        case _ => throw new RuntimeException("More than 1 result back from a limit 1 query")
      }
    }
  }
}
