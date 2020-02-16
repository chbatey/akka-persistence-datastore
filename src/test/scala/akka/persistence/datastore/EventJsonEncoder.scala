package akka.persistence.datastore

import akka.persistence.datastore.MyPersistentBehavior.Incremented

import play.api.libs.json._
import play.api.libs.functional.syntax._

import scala.reflect.runtime.universe._

class EventJsonEncoder extends JsonEncoder[MyPersistentBehavior.Event] {


    def parse(typeValue: String, obj: JsValue) = typeValue match {
      case "incremented" => incrementedReads.reads(obj)
      case _ => JsError(s"Cannot deserialized type $typeValue")
    }
  
    def incrementedReads: Reads[MyPersistentBehavior.Incremented] = 
      (JsPath \ "value").read[Int].map(MyPersistentBehavior.Incremented.apply _)
    

    override def fromJson: PartialFunction[String, MyPersistentBehavior.Event] = { json =>
      val jsonObj = Json.parse(json)
      (jsonObj \ "type").toEither.fold(
        err => ???,
        rawJsonField => rawJsonField.validate[String].fold(
          err => ???,
          typeValue => parse(typeValue, jsonObj).get
        )
      )
    }

    override def parseJson: PartialFunction[MyPersistentBehavior.Event,String] = {
      case Incremented(value) => Json.stringify(Json.obj(
        ("value", JsNumber(value)),
        ("type", JsString("incremented")),
      ))
    }
}

object EventJsonEncoder {
  // implicit val myTypeTag: TypeTag[MyPersistentBehavior.Event] = typeTag[MyPersistentBehavior.Event]
}
