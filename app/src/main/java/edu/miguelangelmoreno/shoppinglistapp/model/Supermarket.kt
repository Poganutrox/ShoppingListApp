package edu.miguelangelmoreno.shoppinglistapp.model

data class Supermarket(
    val id: Int?,
    val name: String,
    val icon: Int,
){
    override fun toString(): String {
        return name
    }
}
