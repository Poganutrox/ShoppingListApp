package edu.miguelangelmoreno.shoppinglistapp.data.repository

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import edu.miguelangelmoreno.shoppinglistapp.data.datasource.APIDataSource
import edu.miguelangelmoreno.shoppinglistapp.data.service.ShoppingListDAO
import edu.miguelangelmoreno.shoppinglistapp.entity.ProductEntity
import edu.miguelangelmoreno.shoppinglistapp.model.Product
import edu.miguelangelmoreno.shoppinglistapp.model.responses.PageResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val dataSource: APIDataSource,
    private val dao : ShoppingListDAO
) {
    suspend fun fetchProducts(
        page: Int, pageSize : Int, productName: String?, categoryId: Int?,
        supermarketIds: Set<Int>?, onSale: Boolean?
    ) = dataSource.getProducts(page,pageSize, productName, categoryId, supermarketIds, onSale)


    suspend fun getProductByIdFromDB(productId: String): ProductEntity = dao.getProductById(productId)

    suspend fun saveProductInDB(product: ProductEntity) {
        dao.saveProduct(product)
    }
    suspend fun insertProductsInDB(products: List<ProductEntity>){
        dao.insertProducts(products)
    }

    suspend fun clearAllProductsFromDB(){
        dao.clearAllProducts()
    }
    suspend fun countAllProductsFromDB() = dao.countAllProducts()

    suspend fun getTimesProductAddedList(productId: String, userId: Int) =
        dataSource.getTimesProductAddedList(productId, userId)

    suspend fun getPriceVariation(
        productId: String,
        userId: Int
    ) = dataSource.getPriceVariation(productId, userId)

    suspend fun setProductFavourite(
        productId: String,
        userId: Int
    ) = dataSource.setProductFavourite(productId, userId)

    suspend fun getFavouriteProduct(
        userId: Int
    ) = dataSource.getFavouriteProduct(userId)


}