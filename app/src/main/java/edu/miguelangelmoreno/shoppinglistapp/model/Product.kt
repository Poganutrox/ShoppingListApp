package edu.miguelangelmoreno.shoppinglistapp.model


import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class Product(
    val id: String,
    val categoryId : Int,
    val supermarketId: Int,
    val name: String,
    val image: String,
    val onSale: Boolean,
    var priceHistories: List<PriceHistory>,
    var isFavourite : Boolean,
    var timesInList :  Long,
    var priceVariation : Double
){
    constructor() : this(
        id = "",
        categoryId = -1,
        supermarketId = -1,
        name = "",
        image = "",
        onSale = false,
        isFavourite = false,
        priceVariation = 0.0,
        timesInList = 0,
        priceHistories = emptyList()
    )
}