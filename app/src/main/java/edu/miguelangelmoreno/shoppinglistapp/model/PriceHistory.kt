package edu.miguelangelmoreno.shoppinglistapp.model

import java.time.LocalDate

data class PriceHistory(
    val productId : String,
    val date: String,
    val price: Double,
    val salePrice: Double,
    val bulkPrice: Double
)
