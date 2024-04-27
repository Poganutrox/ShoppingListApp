package edu.miguelangelmoreno.shoppinglistapp.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("product")
data class ProductEntity (
    @PrimaryKey
    val id : String,
    @ColumnInfo("category_id")
    val categoryId : Int,
    @ColumnInfo("supermarket_id")
    val supermarketId: Int,
    val name: String,
    val image: String,
    val onSale: Boolean
)