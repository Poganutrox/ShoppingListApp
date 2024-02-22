package edu.miguelangelmoreno.shoppinglistapp.ui.fragments.lists

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.miguelangelmoreno.shoppinglistapp.data.ShoppingListRepository
import edu.miguelangelmoreno.shoppinglistapp.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ListsViewModel @Inject constructor(
    repository : ShoppingListRepository
) : ViewModel() {
    private var _currentProducts = repository.fetchProducts()
    val currentProducts: Flow<List<Product>>
        get() = _currentProducts
}