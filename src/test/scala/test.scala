import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._

import play.api.libs.json.{Json => PlayJson, _}

import anyval.AnyVal

case class UserId(value: Int) extends AnyVal

@AnyVal case class ProductId(value: Int)

object ProductId {
  def foo = 3
}

@AnyVal case class Foo(value: Int) extends AnyVal {
  def wow = "hello"
  override def toString = "yolo"
}

object UserId {
  
  implicit val encode: Encoder[UserId] = Encoder[Int].contramap(_.value)
  implicit val decode: Decoder[UserId] = Decoder[Int].map(UserId(_))
  
  implicit val format: Format[UserId] = new Format[UserId] {
    def reads(json: JsValue): JsResult[UserId] = json match {
      case JsNumber(bigDecimal) => JsSuccess(UserId(bigDecimal.toInt))
      case _ => JsError("Expected an integer")
    }
    def writes(userId: UserId): JsValue = JsNumber(BigDecimal(userId.value.toString))
  }
}

case class Order(userId: UserId, productName: String)

object Test {

  val order = Order(UserId(123), "widget")

  val circeJson = order.asJson

  implicit val orderFormat: Format[Order] = PlayJson.format[Order]
  val playJson = PlayJson.toJson(order)
}
