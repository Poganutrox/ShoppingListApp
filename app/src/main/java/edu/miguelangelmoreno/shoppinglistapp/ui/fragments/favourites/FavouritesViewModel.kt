package edu.miguelangelmoreno.shoppinglistapp.ui.fragments.favourites

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.miguelangelmoreno.shoppinglistapp.ShoppingListApplication
import edu.miguelangelmoreno.shoppinglistapp.data.repository.PriceHistoryRepository
import edu.miguelangelmoreno.shoppinglistapp.data.repository.ProductRepository
import edu.miguelangelmoreno.shoppinglistapp.entity.PriceHistoryEntity
import edu.miguelangelmoreno.shoppinglistapp.entity.ProductEntity
import edu.miguelangelmoreno.shoppinglistapp.model.Product
import edu.miguelangelmoreno.shoppinglistapp.utils.mappers.toPriceHistoryEntity
import edu.miguelangelmoreno.shoppinglistapp.utils.mappers.toProductEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val productRepo: ProductRepository,
    private val priceHistoryRepository: PriceHistoryRepository) :
    ViewModel() {
    private val loggedUser = ShoppingListApplication.userPrefs.getLoggedUser()

    private val _favouriteList = MutableStateFlow(emptyList<Product>())

    val favouriteList: StateFlow<List<Product>>
        get() = _favouriteList

    private val _favouriteState = MutableStateFlow(FavouriteState())
    val favouriteState : StateFlow<FavouriteState>
        get() = _favouriteState


    fun getFavouriteList(){
        viewModelScope.launch {
            _favouriteState.value = FavouriteState(isLoading = true)
            val response = productRepo.getFavouriteProduct(loggedUser.id!!)
            if(response.isSuccessful){
                val list = response.body()!!
                var priceHistoryEntityList = mutableListOf<PriceHistoryEntity>()
                list.forEach { product ->
                    product.priceHistories
                        .forEach { priceHistory ->
                            priceHistoryEntityList.add(priceHistory.toPriceHistoryEntity(product.id))
                        }
                }
                productRepo.insertProductsInDB(list.map {
                    it.toProductEntity()
                })

                priceHistoryRepository.insertPrices(priceHistoryEntityList)
                _favouriteList.value = list
            }
            _favouriteState.value = FavouriteState(isLoading = false)
        }
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
