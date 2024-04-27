package edu.miguelangelmoreno.shoppinglistapp.ui.fragments.productdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.miguelangelmoreno.shoppinglistapp.data.repository.PriceHistoryRepository
import edu.miguelangelmoreno.shoppinglistapp.data.repository.ProductRepository
import edu.miguelangelmoreno.shoppinglistapp.model.PriceHistory
import edu.miguelangelmoreno.shoppinglistapp.model.Product
import edu.miguelangelmoreno.shoppinglistapp.utils.mappers.toPriceHistory
import edu.miguelangelmoreno.shoppinglistapp.utils.mappers.toProduct
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val priceHistoryRepo: PriceHistoryRepository,
    private val productRepo : ProductRepository)
    : ViewModel() {
    private var _priceHistory = MutableStateFlow(emptyList<PriceHistory>())
    private var _product = MutableStateFlow(Product())
    val priceHistory : StateFlow<List<PriceHistory>>
        get()  =  _priceHistory
    val product : StateFlow<Product>
        get()  =  _product

    fun getClickedProduct(productId : String){
        viewModelScope.launch {
            val priceHistoryEntity = priceHistoryRepo.getPriceByProductId(productId)
            val priceHistory = priceHistoryEntity.map { it.toPriceHistory(productId) }
            _priceHistory.value = priceHistory

            val productEntity = productRepo.getProductByIdFromDB(productId)
            val product = productEntity.toProduct()
            _product.value = product
        }
    }
}