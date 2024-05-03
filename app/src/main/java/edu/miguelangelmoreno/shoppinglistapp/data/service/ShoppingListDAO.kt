package edu.miguelangelmoreno.shoppinglistapp.data.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import edu.miguelangelmoreno.shoppinglistapp.entity.PriceHistoryEntity
import edu.miguelangelmoreno.shoppinglistapp.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDAO {
    @Transaction
    @Query("SELECT * FROM price_history WHERE product_id = :productId ORDER BY date ASC")
    suspend fun getPriceByProductId(productId: String): List<PriceHistoryEntity>

    @Insert(entity = PriceHistoryEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrices(prices: List<PriceHistoryEntity>)
    @Transaction
    @Query("DELETE FROM price_history")
    suspend fun clearAllPrices()

    @Query("SELECT COUNT(*) FROM price_history")
    suspend fun countAllPrices() : Int

    @Transaction
    @Query("SELECT * FROM product WHERE id = :productId")
    suspend fun getProductById(productId: String): ProductEntity

    @Insert(entity = ProductEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)
    @Insert(entity = ProductEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProduct(product: ProductEntity)
    @Transaction
    @Query("DELETE FROM product")
    suspend fun clearAllProducts()

    @Query("SELECT COUNT(*) FROM product")
    suspend fun countAllProducts() : Int

    @Transaction
    @Query("SELECT * FROM product WHERE isFavourite = 1")
    fun getAllFavourites() : Flow<List<ProductEntity>>

}