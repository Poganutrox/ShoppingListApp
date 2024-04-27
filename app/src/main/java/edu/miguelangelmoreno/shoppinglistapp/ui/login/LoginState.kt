package edu.miguelangelmoreno.shoppinglistapp.ui.login

data class LoginState(
    val emailIsValid: Boolean = false,
    val passwordIsValid: Boolean = false,
    val isSuccessful: Boolean = false,
    val isLoading: Boolean = false,
    val emailErrorMessage : String? = null,
    val passwordErrorMessage : String? = null,
    val loginErrorMessage : Int? = null
)