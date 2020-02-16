package akka.persistence.datastore

import scala.reflect.runtime.universe._

trait JsonEncoder[T] {


  def cast(data: Any)(implicit typeTag: TypeTag[T]): T = data.asInstanceOf[T]

  
  protected def parseJson: PartialFunction[T, String]

  /**
    * A partial function that serializes an event to a json representation
    * @return the json representation
    */
  def toJson(data: Any)(implicit typeTag: TypeTag[T]): String = parseJson(cast(data))


  /**
    * A partial function that deserializes an event from some json representation
    * @return the event
    */
  def fromJson: PartialFunction[(String), T]

}

object NoneJsonEncoder extends JsonEncoder[Any] {

  override def parseJson: PartialFunction[Any,String] = PartialFunction.empty[Any, String]

  override def fromJson: PartialFunction[(String), Any] =
    PartialFunction.empty[(String), Any]
}
