package edu.miguelangelmoreno.shoppinglistapp.data.response

sealed class FirebaseFirestoreResponse {
    data object Success : FirebaseFirestoreResponse()
    data class Error(val message : String) : FirebaseFirestoreResponse()
}