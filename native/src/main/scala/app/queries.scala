package app

import tables._
import slick.driver.H2Driver.api._

object queries {

  def insertCustomer(c: Customer): DBIO[Long] =
    (customers returning customers.map(_.id)) += c

  def insertOrder(o: Order): DBIO[Long] =
    (orders returning orders.map(_.id)) += o

  def insertProduct(p: Product): DBIO[Long] =
    (products returning products.map(_.id)) += p

  def addProductToOrder(orderId: Long, productId: Long): DBIO[Int] =
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

  def customerOrdersTotal(customerId: Long): DBIO[Option[BigDecimal]] =
    (for {
      o <- customerOrdersQ(customerId)
      p <- orderProductsPriceQ(o.id)
    } yield p).sum.result
}
