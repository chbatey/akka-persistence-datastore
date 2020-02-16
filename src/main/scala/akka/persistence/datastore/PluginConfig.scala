package akka.persistence.datastore

import akka.actor.typed.ActorSystem
import com.typesafe.config.Config

object PluginConfig {
  def apply(system: ActorSystem[_]) = new PluginConfig(system.settings.config)

  def apply(config: Config) = new PluginConfig(config)

  def asOption(s: String): Option[String] = if (s.isEmpty) None else Some(s)

  def newInstance(clazz: String): JsonEncoder[_] =
    Thread.currentThread().getContextClassLoader.loadClass(clazz).asInstanceOf[Class[_ <: JsonEncoder[_]]].getDeclaredConstructor().newInstance()

}

class PluginConfig(systemConfig: Config) {
  private val config = systemConfig.getConfig("google-cloud-datastore-persistence")

  def eventEncoder: JsonEncoder[_] = PluginConfig
    .asOption(config.getString("event.encoder"))
    .fold[JsonEncoder[_]](NoneJsonEncoder)(clazz => PluginConfig.newInstance(clazz))
}
