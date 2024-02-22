package edu.miguelangelmoreno.shoppinglistapp.model


import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("currentPrice")
    val currentPrice: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("previousPrice")
    val previousPrice: Any,
    @SerializedName("size")
    val size: String,
    @SerializedName("supermarket")
    val supermarket: Int,
    @SerializedName("unit")
    val unit: String
)