package akka.persistence.datastore

import akka.actor.typed.ActorSystem
import com.typesafe.config.Config

object PluginConfig {
  def apply(system: ActorSystem[_]) = new PluginConfig(system.settings.config)

  def apply(config: Config) = new PluginConfig(config)

  def asOption(s: String): Option[String] = if (s.isEmpty) None else Some(s)

  def newInstance(clazz: String): EventEncoder[_] =
    Thread.currentThread().getContextClassLoader.loadClass(clazz).asInstanceOf[Class[_ <: EventEncoder[_]]].getDeclaredConstructor().newInstance()

}

class PluginConfig(systemConfig: Config) {
  private val config = systemConfig.getConfig("google-cloud-datastore-persistence")

  def eventEncoder: EventEncoder[_] = PluginConfig
    .asOption(config.getString("event.encoder"))
    .fold[EventEncoder[_]](NoneJsonEncoder)(clazz => PluginConfig.newInstance(clazz))
}
