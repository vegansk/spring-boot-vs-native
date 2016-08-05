package app.db

import app.models.Product
import org.springframework.data.repository.CrudRepository

trait ProductsRepository extends CrudRepository[Product, java.lang.Long] {}
