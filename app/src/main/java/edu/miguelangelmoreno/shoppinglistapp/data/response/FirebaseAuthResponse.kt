package edu.miguelangelmoreno.shoppinglistapp.data.response

sealed class FirebaseAuthResponse {
    data object Success : FirebaseAuthResponse()
    data object EmailAlreadyExists : FirebaseAuthResponse()
    data object UserNotExists : FirebaseAuthResponse()
    data object InvalidCredentials : FirebaseAuthResponse()
    data class Error(val message : String) : FirebaseAuthResponse()

}