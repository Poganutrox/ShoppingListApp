package edu.miguelangelmoreno.shoppinglistapp.ui.fragments.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.miguelangelmoreno.shoppinglistapp.ShoppingListApplication.Companion.userPrefs
import edu.miguelangelmoreno.shoppinglistapp.data.datasource.PagingDataSource
import edu.miguelangelmoreno.shoppinglistapp.data.repository.PriceHistoryRepository
import edu.miguelangelmoreno.shoppinglistapp.data.repository.ProductRepository
import edu.miguelangelmoreno.shoppinglistapp.data.repository.UserRepository
import edu.miguelangelmoreno.shoppinglistapp.model.Product
import edu.miguelangelmoreno.shoppinglistapp.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productRepo: ProductRepository,
    private val userRepo: UserRepository,
    private val priceHistoryRepo: PriceHistoryRepository
) : ViewModel() {
    private var productName: String? = null
    private var categoryId: Int? = null
    private var supermarketIds: Set<Int>? = null
    private var onSale: Boolean = false

    private var _productState = MutableStateFlow(ProductState())
    val productState: StateFlow<ProductState>
        get() = _productState

    private var _productList = Pager(
        config = PagingConfig(
            20,
            enablePlaceholders = true,
            initialLoadSize = 40,
            prefetchDistance = 20
        )
    ) {
        PagingDataSource(productRepo, priceHistoryRepo, productName, categoryId, supermarketIds, onSale)
    }.flow.cachedIn(viewModelScope)

    val productList: Flow<PagingData<Product>>
        get() = _productList


    fun setFilters(
        productName: String?,
        categoryId: Int?,
        supermarketIds: Set<Int>?,
        onSale: Boolean
    ) {
        this.categoryId = if (categoryId == -1) null else categoryId
        this.productName = if (productName.isNullOrBlank()) null else productName
        this.supermarketIds = if (supermarketIds?.isEmpty() == true) null else supermarketIds
        this.onSale = onSale
    }


    fun likeProduct(productId: String) {
        val loggedUser = userPrefs.getLoggedUser()
        updateFavourites(loggedUser, productId)

        viewModelScope.launch {
            val response = userRepo.updateUser(loggedUser)
            if (!response.isSuccessful) {
                updateFavourites(loggedUser, productId)
            }
            resetState()
        }
    }

    private fun updateFavourites(loggedUser: User, productId: String) {
        val updatedFavorites = loggedUser.favouriteProductsId ?: mutableSetOf()

        if (updatedFavorites.contains(productId)) {
            updatedFavorites.remove(productId)
            _productState.value = ProductState(isFavourite = false)
        } else {
            updatedFavorites.add(productId)
            _productState.value = ProductState(isFavourite = true)
        }
        loggedUser.favouriteProductsId = updatedFavorites
        userPrefs.updateLoggedUser(loggedUser)
    }

    private fun resetState() {
        _productState.value = ProductState()
    }


}