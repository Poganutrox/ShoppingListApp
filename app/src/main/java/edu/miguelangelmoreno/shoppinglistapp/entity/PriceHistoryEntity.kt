package edu.miguelangelmoreno.shoppinglistapp.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "price_history", primaryKeys = ["product_id", "date"])
data class PriceHistoryEntity(
    @ColumnInfo(name = "product_id")
    val productId: String,
    val date: String,
    val price: Double,
    val salePrice: Double,
    val bulkPrice: Double
)
