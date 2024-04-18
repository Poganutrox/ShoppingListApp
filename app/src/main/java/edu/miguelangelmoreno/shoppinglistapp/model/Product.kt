package edu.miguelangelmoreno.shoppinglistapp.model


import com.google.gson.annotations.SerializedName

data class Product(
    val id: String,
    val category: Category,
    val supermarket: Supermarket,
    val name: String,
    val image: String,
    val available: Boolean,
    val onSale: Boolean,
    val priceHistories: List<PriceHistory>
)