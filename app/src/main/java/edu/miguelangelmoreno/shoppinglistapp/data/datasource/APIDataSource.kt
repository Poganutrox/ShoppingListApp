package edu.miguelangelmoreno.shoppinglistapp.data.datasource

import edu.miguelangelmoreno.shoppinglistapp.data.service.APIService
import edu.miguelangelmoreno.shoppinglistapp.model.Category
import edu.miguelangelmoreno.shoppinglistapp.model.Product
import edu.miguelangelmoreno.shoppinglistapp.model.ShoppingList
import edu.miguelangelmoreno.shoppinglistapp.model.ShoppingListDTO
import edu.miguelangelmoreno.shoppinglistapp.model.User
import edu.miguelangelmoreno.shoppinglistapp.model.responses.PageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.math.BigDecimal
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
        page: Int, pageSize: Int, productName: String?, categoryId: Int?,
        supermarketIds: Set<Int>?, onSale: Boolean?
    ): PageResponse<Product> =
        api.getProducts(page, pageSize, productName, categoryId, supermarketIds, onSale)

    suspend fun getTimesProductAddedList(productId: String, userId: Int) =
        api.getTimesProductAddedList(productId, userId)

    suspend fun getPriceVariation(
        productId: String,
        userId: Int
    ) = api.getPriceVariation(productId, userId)

    suspend fun setProductFavourite(
        productId: String,
        userId: Int
    ) = api.setProductFavourite(productId, userId)

    suspend fun getFavouriteProduct(userId: Int) = api.getFavouriteProduct(userId)
    suspend fun getUserShoppingLists(userId: Int) = api.getUserShoppingLists(userId)
    suspend fun saveShoppingList(shoppingListDTO: ShoppingListDTO) = api.saveShoppingList(shoppingListDTO)
}