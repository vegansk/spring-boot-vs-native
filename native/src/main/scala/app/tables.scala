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
    def createdAt = column[java.sql.Timestamp]("created_at")
    def customer = foreignKey("customers", customerId, customers)(_.id)

    def * = (id.?, customerId, createdAt) <> (Order.tupled, Order.unapply)
  }

  val orders = TableQuery[Orders]

  class Products(tag: Tag) extends Table[Product](tag, "products") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def price = column[Option[BigDecimal]]("price")

    def * = (id.?, name, price) <> (Product.tupled, Product.unapply)
  }

  val products = TableQuery[Products]

  class OrderProducts(tag: Tag) extends Table[(Long, Long)](tag, "order_products") {
    def orderId = column[Long]("order_id")
    def productId = column[Long]("product_id")
    def order = foreignKey("order_fk", orderId, orders)(_.id)
    def product = foreignKey("product_fk", productId, products)(_.id)

    def * = (orderId, productId)
  }

  val orderProducts = TableQuery[OrderProducts]
}

