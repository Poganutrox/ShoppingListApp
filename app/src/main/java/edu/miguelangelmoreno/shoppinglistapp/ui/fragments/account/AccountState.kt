package edu.miguelangelmoreno.shoppinglistapp.ui.fragments.account

data class AccountState(
    val isLoading : Boolean = false,
    val isUpdated : Boolean = false,
    val errorMessage : Int? = null
)
