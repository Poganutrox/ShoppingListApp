package edu.miguelangelmoreno.shoppinglistapp.ui.fragments.shoppingLists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.miguelangelmoreno.shoppinglistapp.ShoppingListApplication.Companion.userPrefs
import edu.miguelangelmoreno.shoppinglistapp.data.repository.ShoppingListRepository
import edu.miguelangelmoreno.shoppinglistapp.model.ShoppingList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListsViewModel @Inject constructor(private val shoppingListRepo : ShoppingListRepository) : ViewModel() {
    private val userId = userPrefs.getUserId()
    private var _shoppingLists = MutableStateFlow(listOf<ShoppingList>())
    val shoppingLists : StateFlow<List<ShoppingList>>
        get() = _shoppingLists

    init {
        getShoppingLists()
    }
    fun getShoppingLists(){
        viewModelScope.launch {
            val response = shoppingListRepo.getUserShoppingLists(userId!!)
            if(response.isSuccessful){
                _shoppingLists.value = response.body()!!
            }
        }
    }
}