package edu.miguelangelmoreno.shoppinglistapp.domain.service

import edu.miguelangelmoreno.shoppinglistapp.Prefs
import edu.miguelangelmoreno.shoppinglistapp.model.Category
import edu.miguelangelmoreno.shoppinglistapp.model.Product
import edu.miguelangelmoreno.shoppinglistapp.model.User
import edu.miguelangelmoreno.shoppinglistapp.model.responses.ApiResponse
import edu.miguelangelmoreno.shoppinglistapp.model.responses.PageResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface APIService {
    @POST("user/register")
    suspend fun createUser(@Body user : User) : Response<User>
    @POST("user/login")
    suspend fun getAccess(@Body user: User): Response<User>
    @PUT("user/update")
    suspend fun updateUser(@Body user: User) : Response<User>
    @GET("product/findBy")
    suspend fun getProducts(
        @Query("page") page : Int,
        @Query("productName") productName : String?,
        @Query("categoryId") categoryId : Int?,
        @Query("supermarketIds") supermarketIds : Set<Int>?,
        @Query("onSale") onSale : Boolean?,
    ): PageResponse<Product>
    @GET("category")
    suspend fun getCategories(): ApiResponse<List<Category>>


}