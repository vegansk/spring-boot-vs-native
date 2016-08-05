package app.db

import app.models.Order
import org.springframework.data.repository.CrudRepository

trait OrdersRepository extends CrudRepository[Order, java.lang.Long] {}
