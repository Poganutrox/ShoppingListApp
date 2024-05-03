package edu.miguelangelmoreno.shoppinglistapp.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class ShoppingList(
    val id: Int? = null,
    val creatorUser: User? = null,
    val name: String? = null,
    val creationDate: String? = null,
    val status: Boolean? = null,
    val users: Set<User>? = null,
    @SerializedName("shoppinglistProducts")
    var shoppingListProducts : List<ShoppingListProduct>? = null
)
