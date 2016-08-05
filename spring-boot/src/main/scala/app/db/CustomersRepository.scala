package app.db

import app.models.Customer
import org.springframework.data.repository.CrudRepository

trait CustomersRepository extends CrudRepository[Customer, java.lang.Long] {}
