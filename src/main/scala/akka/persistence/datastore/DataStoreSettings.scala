package akka.persistence.datastore

import com.typesafe.config.Config
import com.typesafe.config.Config._

class DataStoreSettings private(config: Config) {

  val project = config.getString("project")
  val credentialsFile = config.getString("credentials-file")
  val requestTimeout = config.getDuration("request-timeout")
  val host = config.getString("host")

}

object DataStoreSettings {
  def apply(config: Config): DataStoreSettings = new DataStoreSettings(config)
}
