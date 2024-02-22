package edu.miguelangelmoreno.shoppinglistapp.domain.service

import edu.miguelangelmoreno.shoppinglistapp.model.Product
import retrofit2.http.GET

interface APIService {
    @GET("api/supermercados/productos")
    suspend fun getProducts(): List<Product>
}