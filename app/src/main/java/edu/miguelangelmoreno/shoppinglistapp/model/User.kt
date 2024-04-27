package edu.miguelangelmoreno.shoppinglistapp.model

import com.google.gson.annotations.SerializedName

data class User(
    val token : String? = null,
    val id : Int? = null,
    var name : String? = null,
    var lastName : String? = null,
    var phone : String? = null,
    var email : String? = null,
    val password : String? = null,
    var favouriteProductsId : MutableSet<String>? = mutableSetOf()
)