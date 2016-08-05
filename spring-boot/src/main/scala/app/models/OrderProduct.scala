package app.models

import javax.persistence._
import scala.beans.BeanProperty

@Embeddable
case class OrderProductPK(
  @BeanProperty
  @_Column(name="order_id")
  var orderId: java.lang.Long,

  @BeanProperty
    @_Column(name="product_id")
  var productId: java.lang.Long
) {
  def this() = this(null, null)
}

@Entity
@Table(name="order_products")
case class OrderProduct(

  @BeanProperty
  @_EmbeddedId
  var id: OrderProductPK,

  @BeanProperty
  @_ManyToOne(optional = false)
  @_JoinColumn(name="order_id", updatable=false, insertable=false)
  var order: Order,

  @BeanProperty
  @_ManyToOne(optional = false)
  @_JoinColumn(name="product_id", updatable=false, insertable=false)
  var product: Product
) {
  def this() = this(null, null, null)
}
