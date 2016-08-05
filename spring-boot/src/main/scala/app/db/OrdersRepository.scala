package app.db

import app.models.Order
import java.lang.Iterable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

trait OrdersRepository extends CrudRepository[Order, java.lang.Long] {
  @Query(value = "select sum(p.price) from order_products op join products p on p.id = op.product_id where op.order_id=?1", nativeQuery = true)
  def orderTotalPrice(orderId: java.lang.Long): java.math.BigDecimal

  def findByCustomerId(customerId: java.lang.Long): Iterable[Order]
}
