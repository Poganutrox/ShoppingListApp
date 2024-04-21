package edu.miguelangelmoreno.shoppinglistapp.ui.fragments.filters

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.miguelangelmoreno.shoppinglistapp.data.ShoppingListRepository
import edu.miguelangelmoreno.shoppinglistapp.model.Category
import edu.miguelangelmoreno.shoppinglistapp.model.responses.ApiResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class FiltersViewModel @Inject constructor(
    repository : ShoppingListRepository
) : ViewModel() {

    private var _currentCategories = repository.getCategories()
    val currentCategories: Flow<ApiResponse<List<Category>>>
        get() = _currentCategories
}