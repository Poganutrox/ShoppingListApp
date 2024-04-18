package edu.miguelangelmoreno.shoppinglistapp.data

import edu.miguelangelmoreno.shoppinglistapp.model.Category
import edu.miguelangelmoreno.shoppinglistapp.model.Product
import edu.miguelangelmoreno.shoppinglistapp.model.User
import edu.miguelangelmoreno.shoppinglistapp.model.responses.ApiResponse
import edu.miguelangelmoreno.shoppinglistapp.model.responses.PageResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShoppingListRepository @Inject constructor(
    private val dataSource: ShoppingListDataSource
) {
    suspend fun checkAccess(user: User): ApiResponse<User> = dataSource.checkAccess(user)
    suspend fun createUser(user: User): ApiResponse<User> = dataSource.createUser(user)
    fun getCategories(): Flow<ApiResponse<List<Category>>> = dataSource.getCategories()
    fun fetchProducts(
        page: Int,
        productName: String?,
        categoryId: Int?,
        supermarketIds: Set<Int>?,
        onSale: Boolean?
    ): Flow<PageResponse<Product>> {
        return dataSource.getProducts(page, productName, categoryId, supermarketIds, onSale)

    }

}