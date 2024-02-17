package edu.miguelangelmoreno.shoppinglistapp.ui.login


sealed class LoginResponse {
    data object Success : LoginResponse()
    data class EmailError(val message: String) : LoginResponse()
    data class PasswordError(val message: String) : LoginResponse()
}