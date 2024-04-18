package edu.miguelangelmoreno.shoppinglistapp.model

data class PriceHistory(
    val date: String,
    val price: Double,
    val salePrice: Double,
    val bulkPrice: Double
)
