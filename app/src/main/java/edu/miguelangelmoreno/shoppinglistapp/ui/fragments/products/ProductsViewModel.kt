package edu.miguelangelmoreno.shoppinglistapp.ui.fragments.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.miguelangelmoreno.shoppinglistapp.ShoppingListApplication.Companion.userPrefs
import edu.miguelangelmoreno.shoppinglistapp.data.repository.PriceHistoryRepository
import edu.miguelangelmoreno.shoppinglistapp.data.repository.ProductRepository
import edu.miguelangelmoreno.shoppinglistapp.entity.ProductEntity
import edu.miguelangelmoreno.shoppinglistapp.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productRepo: ProductRepository,
    private val priceHistoryRepo: PriceHistoryRepository
) : ViewModel() {
    private var productName: String? = null
    private var categoryId: Int? = null
    private var supermarketIds: Set<Int>? = null
    private var onSale: Boolean = false
    private val loggedUser = userPrefs.getLoggedUser()

    private var _productList = Pager(
        config = PagingConfig(
            20,
            initialLoadSize = 40,
            prefetchDistance = 20
        )
    ) {
        PagingDataSource(
            productRepo,
            priceHistoryRepo,
            productName,
            categoryId,
            supermarketIds,
            onSale
        )
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
        viewModelScope.launch {
            val product = productRepo.getProductByIdFromDB(productId)
            product.isFavourite = !product.isFavourite
            saveInLocalDB(product)
            saveInRemoteDB(product)
        }

    }

    private fun saveInRemoteDB(product: ProductEntity) {
        viewModelScope.launch {
            val response = productRepo.setProductFavourite(product.id, loggedUser.id!!)
            if (!response.isSuccessful) {
                product.isFavourite = !product.isFavourite
                productRepo.saveProductInDB(product)
            }
        }
    }

    private fun saveInLocalDB(product: ProductEntity) {
        viewModelScope.launch {
            val favoritesProducts = loggedUser.favouriteProductsId ?: mutableSetOf()

            if (favoritesProducts.contains(product.id)) {
                favoritesProducts.remove(product.id)
            } else {
                favoritesProducts.add(product.id)
            }

            productRepo.saveProductInDB(product)
        }
    }
}