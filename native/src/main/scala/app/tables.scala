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

object queries {
  import tables._

  def insertCustomer(c: Customer): DBIO[Long] =
    (customers returning customers.map(_.id)) += c

  def insertOrder(o: Order): DBIO[Long] =
    (orders returning orders.map(_.id)) += o

  def insertProduct(p: Product): DBIO[Long] =
    (products returning products.map(_.id)) += p

  def addProductToOrder(orderId: Long, productId: Long) =
    orderProducts += (orderId, productId)

  def orderTotalPrice(orderId: Long): DBIO[Option[BigDecimal]] =
    orderProductsPriceQ(orderId).sum.result

  private def orderProductsPriceQ(orderId: Rep[Long]) =
    for {
      op <- orderProducts.filter(_.orderId === orderId)
      p <- op.product
    } yield p.price

  private def customerOrdersQ(customerId: Long) =
    orders.filter(_.customerId === customerId)

  def customerOrders(customerId: Long): DBIO[Seq[Order]] =
    customerOrdersQ(customerId).result

  def productsPaginated(page: Int, pageSize: Int): DBIO[Seq[Product]] =
    products.drop((page - 1) * pageSize).take(pageSize).result

  def customersWhoOrderedTheProduct(productId: Long): DBIO[Seq[Customer]] =
    (for {
      op <- orderProducts.filter(_.productId === productId)
      o <- op.order
      c <- o.customer
    } yield c).distinct.result

  def customerOrdersTotal(customerId: Long) =
    (for {
      o <- customerOrdersQ(customerId)
      p <- orderProductsPriceQ(o.id)
    } yield p).sum.result
}
