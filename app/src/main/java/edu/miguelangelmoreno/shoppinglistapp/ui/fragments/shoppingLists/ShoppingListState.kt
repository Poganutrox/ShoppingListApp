package edu.miguelangelmoreno.shoppinglistapp.ui.fragments.shoppingLists

import androidx.paging.RemoteMediator

data class ShoppingListState(
    val isImportLoading: Boolean = false,
    val isImportSuccess: Boolean = false,
    val isImportReceived : Boolean = false,
    val isShoppingListSuccess: Boolean = false,
    val isShoppingListReceived: Boolean = false,
    val isShoppingListLoading: Boolean = false,
)
