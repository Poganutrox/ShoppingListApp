package edu.miguelangelmoreno.shoppinglistapp.ui.signup

import edu.miguelangelmoreno.shoppinglistapp.ui.login.LoginResponse

sealed class SignUpResponse {
    data object Success : SignUpResponse()
    data class NameError(val message: String) : SignUpResponse()
    data class LastNameError(val message: String) : SignUpResponse()
    data class PhoneError(val message: String) : SignUpResponse()
    data class RepeatPasswordError(val message: String) : SignUpResponse()
}