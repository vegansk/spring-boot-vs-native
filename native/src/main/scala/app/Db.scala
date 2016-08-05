package app

import slick.driver.H2Driver.api._

object tables {

  class Customers(tag: Tag) extends Table[Customer](tag, "customers") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")

    def * = (id.?, name) <> (Customer.tupled, Customer.unapply)
  }

  val customers = TableQuery[Customers]

  class Orders(tag: Tag) extends Table[Order](tag, "orders") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def customerId = column[Long]("customer_id")
    def customer = foreignKey("customers", customerId, customers)(_.id)

    def * = (id.?, customerId) <> (Order.tupled, Order.unapply)
  }

  val orders = TableQuery[Orders]

  class Products(tag: Tag) extends Table[Product](tag, "products") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")

    def * = (id.?, name) <> (Product.tupled, Product.unapply)
  }

  val products = TableQuery[Products]
}
