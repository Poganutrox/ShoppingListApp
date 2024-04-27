package edu.miguelangelmoreno.shoppinglistapp.data.datasource

import edu.miguelangelmoreno.shoppinglistapp.data.service.APIService
import edu.miguelangelmoreno.shoppinglistapp.model.Category
import edu.miguelangelmoreno.shoppinglistapp.model.Product
import edu.miguelangelmoreno.shoppinglistapp.model.User
import edu.miguelangelmoreno.shoppinglistapp.model.responses.PageResponse
import retrofit2.Response
import javax.inject.Inject

class APIDataSource @Inject constructor(
    private val api: APIService
) {
    suspend fun checkAccess(user: User): Response<User> = api.getAccess(user)
    suspend fun createUser(user: User): Response<User> = api.createUser(user)
    suspend fun updateUser(user: User): Response<User> = api.updateUser(user)
    suspend fun getCategories(): Response<List<Category>> = api.getCategories()
    suspend fun getProductById(productId: String): Response<Product> = api.getProductById(productId)
    suspend fun getProducts(
        page: Int, pageSize : Int, productName: String?, categoryId: Int?,
        supermarketIds: Set<Int>?, onSale: Boolean?
    ): PageResponse<Product> =
        api.getProducts(page,pageSize, productName, categoryId, supermarketIds, onSale)
}