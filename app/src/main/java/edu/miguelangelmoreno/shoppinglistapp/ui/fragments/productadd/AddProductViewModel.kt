package edu.miguelangelmoreno.shoppinglistapp.ui.fragments.productadd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.miguelangelmoreno.shoppinglistapp.ShoppingListApplication.Companion.addPrefs
import edu.miguelangelmoreno.shoppinglistapp.data.repository.PriceHistoryRepository
import edu.miguelangelmoreno.shoppinglistapp.data.repository.ProductRepository
import edu.miguelangelmoreno.shoppinglistapp.data.repository.ShoppingListRepository
import edu.miguelangelmoreno.shoppinglistapp.model.ShoppingListDTO
import edu.miguelangelmoreno.shoppinglistapp.model.ShoppingListProduct
import edu.miguelangelmoreno.shoppinglistapp.utils.mappers.toPriceHistory
import edu.miguelangelmoreno.shoppinglistapp.utils.mappers.toProduct
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val shoppingListRepo: ShoppingListRepository,
    private val productRepo: ProductRepository,
    private val priceRepo : PriceHistoryRepository
) : ViewModel() {
    private var _shoppingProductList = MutableStateFlow(listOf<ShoppingListProduct>())
    val shoppingProductList: StateFlow<List<ShoppingListProduct>>
        get() = _shoppingProductList

    private var _addProductState = MutableStateFlow(AddProductState())
    val addProductState: StateFlow<AddProductState>
        get() = _addProductState

    fun getShoppingListProducts() {
        viewModelScope.launch {
            val mapProductList = addPrefs.getAllProductList()
            var productList = mutableListOf<ShoppingListProduct>()
            for (key in mapProductList.keys) {
                val quantity = mapProductList[key]
                val prices = priceRepo.getPriceByProductId(key).map { it.toPriceHistory(key) }
                val product = productRepo.getProductByIdFromDB(key).toProduct()
                product.priceHistories = prices
                productList.add(ShoppingListProduct(product, quantity!!))
            }
            _shoppingProductList.value = productList
        }
    }

    fun saveShoppingList(shoppingList : ShoppingListDTO){
        viewModelScope.launch {
            _addProductState.value = AddProductState(isLoading = true)
            val response = shoppingListRepo.saveShoppingList(shoppingList)
            if(response.isSuccessful){
                _addProductState.value = AddProductState(isReceived = true, isSuccess = true)
            }else{
                _addProductState.value = AddProductState(isReceived = true, isSuccess = false)
            }

            _addProductState.value = AddProductState(isLoading = false)
        }
    }
}