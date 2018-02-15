package akka.persistence.datastore

import com.typesafe.config.Config

class DataStoreSettings private(config: Config) {

  val project = config.getString("project")
  val credentialsFile = config.getString("credentials-file")
  val requestTimeout = config.getDuration("request-timeout")

}

object DataStoreSettings {
  def apply(config: Config): DataStoreSettings = new DataStoreSettings(config)
}
