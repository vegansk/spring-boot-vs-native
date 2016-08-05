package app

case class Greeting(
  id: Long,
  content: String
)

case class Customer(id: Option[Long], name: String)
case class Order(id: Option[Long], customerId: Long)
case class Product(id: Option[Long], name: String)
