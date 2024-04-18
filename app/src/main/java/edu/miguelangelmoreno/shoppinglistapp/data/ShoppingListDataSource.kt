package edu.miguelangelmoreno.shoppinglistapp.data

import edu.miguelangelmoreno.shoppinglistapp.domain.service.APIService
import edu.miguelangelmoreno.shoppinglistapp.model.Category
import edu.miguelangelmoreno.shoppinglistapp.model.User
import edu.miguelangelmoreno.shoppinglistapp.model.responses.ApiResponse
import kotlinx.coroutines.flow.flow
import retrofit2.http.Query
import javax.inject.Inject

class ShoppingListDataSource @Inject constructor(
    private val api: APIService
) {
    suspend fun checkAccess(user: User): ApiResponse<User> = api.getAccess(user)
    suspend fun createUser(user: User): ApiResponse<User> = api.createUser(user)
    fun getCategories() = flow {
        emit(api.getCategories())
    }

    fun getProducts(
        page: Int,
        productName: String?,
        categoryId: Int?,
        supermarketIds: Set<Int>?,
        onSale: Boolean?
    ) = flow {
        emit(api.getProducts(page, productName, categoryId, supermarketIds, onSale))
    }
}