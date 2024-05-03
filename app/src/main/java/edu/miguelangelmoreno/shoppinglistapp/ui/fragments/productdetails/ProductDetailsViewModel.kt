package edu.miguelangelmoreno.shoppinglistapp.ui.fragments.productdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.miguelangelmoreno.shoppinglistapp.ShoppingListApplication
import edu.miguelangelmoreno.shoppinglistapp.ShoppingListApplication.Companion.userPrefs
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
    private val productRepo: ProductRepository
) : ViewModel() {
    private var _detailsState = MutableStateFlow(DetailsState())
    private var _priceHistory = MutableStateFlow(emptyList<PriceHistory>())
    private var _product = MutableStateFlow(Product())
    val priceHistory: StateFlow<List<PriceHistory>>
        get() = _priceHistory
    val product: StateFlow<Product>
        get() = _product
    val detailsState: StateFlow<DetailsState>
        get() = _detailsState

    fun getClickedProduct(productId: String) {
        val userId = userPrefs.getUserId() ?: -1
        viewModelScope.launch {
            _detailsState.value = DetailsState(isLoading = true)
            //Calls to API and DB
            val priceHistoryEntity = priceHistoryRepo.getPriceByProductId(productId)
            val productEntity = productRepo.getProductByIdFromDB(productId)
            val timesInList = productRepo.getTimesProductAddedList(productId, userId)
            val priceVariation = productRepo.getPriceVariation(productId, userId)

            val priceHistory = priceHistoryEntity.map { it.toPriceHistory(productId) }
            val product = productEntity.toProduct()

            if (timesInList.isSuccessful && priceVariation.isSuccessful) {
                product.timesInList = timesInList.body()!!
                product.priceVariation = priceVariation.body()!!
                _product.value = product
                _priceHistory.value = priceHistory
                _detailsState.value = DetailsState(isSuccessful = true)
            } else {
                _detailsState.value = DetailsState(isSuccessful = false)
            }
            _detailsState.value = DetailsState(isLoading = false)
        }
    }
}