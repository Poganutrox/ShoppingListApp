package edu.miguelangelmoreno.shoppinglistapp.model

data class User(
    val id : Integer? = null,
    val name : String? = null,
    val lastName : String? = null,
    val phone : String? = null,
    val email : String,
    val password : String
)