package app.db

import app.models.{ OrderProduct, OrderProductPK }
import org.springframework.data.repository.CrudRepository

trait OrderProductsRepository extends CrudRepository[OrderProduct, OrderProductPK] {}
