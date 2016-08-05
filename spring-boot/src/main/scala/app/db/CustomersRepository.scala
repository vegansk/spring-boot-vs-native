package app.db

import app.models.Customer
import java.lang.Iterable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

trait CustomersRepository extends CrudRepository[Customer, java.lang.Long] {
  @Query(value = """select * from customers where id in (select distinct(o.customer_id) from orders o join order_products op on o.id = op.order_id where op.product_id=?1)""", nativeQuery = true)
  def customersWhoOrderedTheProduct(productId: java.lang.Long): Iterable[Customer]

  @Query(value = """select sum(p.price) from order_products op join products p on op.product_id = p.id join orders o on op.order_id = o.id where o.customer_id = ?1""", nativeQuery = true)
  def customerOrdersTotal(customerId: java.lang.Long): java.math.BigDecimal
}
