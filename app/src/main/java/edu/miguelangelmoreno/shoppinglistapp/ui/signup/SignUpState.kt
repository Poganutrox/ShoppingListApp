package edu.miguelangelmoreno.shoppinglistapp.ui.signup

data class SignUpState (
    val nameIsValid : Boolean = false,
    val lastNameIsValid : Boolean = false,
    val emailIsValid : Boolean = false,
    val phoneIsValid : Boolean = true,
    val passwordIsValid : Boolean = false,
    val passwordRepeatedIsValid : Boolean = false,
    val isSuccessful : Boolean = false,
    val isLoading : Boolean = false
)