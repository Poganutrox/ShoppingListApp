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
class ShoppingListsViewModel @Inject constructor(private val shoppingListRepo: ShoppingListRepository) :
    ViewModel() {
    private val userId = userPrefs.getUserId()!!
    private var _shoppingLists = MutableStateFlow(listOf<ShoppingList>())
    val shoppingLists: StateFlow<List<ShoppingList>>
        get() = _shoppingLists

    private var _shoppingListState = MutableStateFlow(ShoppingListState())
    val shoppingListState: StateFlow<ShoppingListState>
        get() = _shoppingListState

    init {
        getShoppingLists()
    }

    fun importList(uniqueCode: String) {
        viewModelScope.launch {
            _shoppingListState.value = ShoppingListState(isImportLoading = true)
            val response = shoppingListRepo.importShoppingList(userId, uniqueCode)
            if (response.isSuccessful) {
                _shoppingListState.value =
                    ShoppingListState(isImportReceived = true, isImportSuccess = true)
            }
            _shoppingListState.value = ShoppingListState(isImportLoading = false)
            clearState()
        }
    }

    private fun getShoppingLists() {
        viewModelScope.launch {
            _shoppingListState.value = ShoppingListState(isShoppingListLoading = true)
            val response = shoppingListRepo.getUserShoppingLists(userId)
            if (response.isSuccessful) {
                _shoppingListState.value =
                    ShoppingListState(isShoppingListReceived = true, isShoppingListSuccess = true)
                _shoppingLists.value = response.body()!!
            } else {
                _shoppingListState.value =
                    ShoppingListState(isShoppingListReceived = true, isShoppingListSuccess = false)
            }
            _shoppingListState.value = ShoppingListState(isShoppingListLoading = false)
        }
    }

    private fun clearState() {
        _shoppingListState.value = ShoppingListState()
    }

    fun deleteShoppingList(shoppingList: ShoppingList?, userId : Int) {
        viewModelScope.launch {
            _shoppingListState.value = ShoppingListState(isShoppingListLoading = true)
            val response = shoppingListRepo.deleteShoppingList(shoppingList?.id!!, userId)
            if (response.isSuccessful) {
                _shoppingListState.value =
                    ShoppingListState(isShoppingListReceived = true, isShoppingListSuccess = true)
                getShoppingLists()
            }
            _shoppingListState.value = ShoppingListState(isShoppingListLoading = false)
        }
    }

    fun removeChanges(shoppingList: ShoppingList?, userId : Int) {
        viewModelScope.launch {
            _shoppingListState.value = ShoppingListState(isShoppingListLoading = true)
            val response = shoppingListRepo.removeChanges(shoppingList?.id!!, userId)
            if (response.isSuccessful) {
                _shoppingListState.value =
                    ShoppingListState(isShoppingListReceived = true, isShoppingListSuccess = true)
                getShoppingLists()
            }
            _shoppingListState.value = ShoppingListState(isShoppingListLoading = false)
        }
    }
}