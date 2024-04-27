package edu.miguelangelmoreno.shoppinglistapp.model


import com.google.gson.annotations.SerializedName

data class Product(
    val id: String,
    val categoryId : Int,
    val supermarketId: Int,
    val name: String,
    val image: String,
    val onSale: Boolean,
    val priceHistories: List<PriceHistory>
){
    constructor() : this(
        id = "",
        categoryId = -1,
        supermarketId = -1,
        name = "",
        image = "",
        onSale = false,
        priceHistories = emptyList()
    )
}