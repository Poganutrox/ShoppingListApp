package edu.miguelangelmoreno.shoppinglistapp.data

import edu.miguelangelmoreno.shoppinglistapp.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShoppingListRepository @Inject constructor(
    private val dataSource : ShoppingListDataSource
) {
    fun fetchProducts(): Flow<List<Product>> {
        return dataSource.getProducts()
    }
}