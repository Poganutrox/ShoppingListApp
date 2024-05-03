package edu.miguelangelmoreno.shoppinglistapp.model

data class ShoppingListDTO(
    var id : Int? = null,
    val name : String,
    val creatorUserId : Int,
    val status : Boolean,
    val shoppingListProducts : Map<String, Int>,
    val users : List<Int>
)
