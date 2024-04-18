package edu.miguelangelmoreno.shoppinglistapp.ui.fragments.products

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.miguelangelmoreno.shoppinglistapp.data.ShoppingListRepository
import edu.miguelangelmoreno.shoppinglistapp.model.Product
import edu.miguelangelmoreno.shoppinglistapp.model.responses.PageResponse
import edu.miguelangelmoreno.shoppinglistapp.ui.login.LoginState
import edu.miguelangelmoreno.shoppinglistapp.utils.checkConnection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val repository: ShoppingListRepository
) : ViewModel() {
    private var page: Int = 0
    private var productName: String? = null
    private var categoryId: Int? = null
    private var supermarketIds: Set<Int>? = null
    private var onSale: Boolean? = false
    private var _currentProducts = repository.fetchProducts(
        page = page,
        productName = productName,
        categoryId = categoryId,
        supermarketIds = supermarketIds,
        onSale = onSale
    )
    val currentProducts: Flow<PageResponse<Product>>
        get() = _currentProducts

    fun setFilters(
        page: Int,
        productName: String?,
        categoryId: Int?,
        supermarketIds: Set<Int>?,
        onSale: Boolean?
    ) {
        this.page = page
        this.categoryId = if (categoryId == -1) null else categoryId
        this.productName = if (productName.isNullOrBlank()) null else productName
        this.supermarketIds = if(supermarketIds?.isEmpty() == true) null else supermarketIds
        this.onSale = onSale

        Log.i("page", this.page.toString())
        Log.i("productName", this.productName ?: "null")
        Log.i("categoryId", this.categoryId.toString())
        Log.i("supermarketIds", this.supermarketIds.toString())
        Log.i("onSale", this.onSale.toString())

        _currentProducts = repository.fetchProducts(
            page = page,
            productName = productName,
            categoryId = categoryId,
            supermarketIds = supermarketIds,
            onSale = onSale
        )


    }

}