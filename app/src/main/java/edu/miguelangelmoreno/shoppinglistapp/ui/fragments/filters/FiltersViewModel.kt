package edu.miguelangelmoreno.shoppinglistapp.ui.fragments.filters

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.miguelangelmoreno.shoppinglistapp.data.repository.CategoryRepository
import edu.miguelangelmoreno.shoppinglistapp.model.Category
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class FiltersViewModel @Inject constructor(
    categoryRepository : CategoryRepository
) : ViewModel() {

    private var _currentCategories = categoryRepository.getCategories()
    val currentCategories: Flow<Response<List<Category>>>
        get() = _currentCategories
}