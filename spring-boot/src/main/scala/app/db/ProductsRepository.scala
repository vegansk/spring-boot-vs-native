package app.db

import app.models.Product
import org.springframework.data.repository.PagingAndSortingRepository

trait ProductsRepository extends PagingAndSortingRepository[Product, java.lang.Long] {}
