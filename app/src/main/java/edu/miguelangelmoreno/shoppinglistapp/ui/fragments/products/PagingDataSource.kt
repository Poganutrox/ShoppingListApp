package edu.miguelangelmoreno.shoppinglistapp.ui.fragments.products

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import edu.miguelangelmoreno.shoppinglistapp.ShoppingListApplication.Companion.userPrefs
import edu.miguelangelmoreno.shoppinglistapp.data.repository.PriceHistoryRepository
import edu.miguelangelmoreno.shoppinglistapp.data.repository.ProductRepository
import edu.miguelangelmoreno.shoppinglistapp.entity.PriceHistoryEntity
import edu.miguelangelmoreno.shoppinglistapp.model.Product
import edu.miguelangelmoreno.shoppinglistapp.utils.mappers.toPriceHistoryEntity
import edu.miguelangelmoreno.shoppinglistapp.utils.mappers.toProductEntity

class PagingDataSource(
    private val productRepository: ProductRepository,
    private val priceHistoryRepository: PriceHistoryRepository,
    private val productName: String?,
    private val categoryId: Int?,
    private val supermarketIds: Set<Int>?,
    private val onSale: Boolean,
    private val alphabeticSort: Int? = null,
    private val priceSort: Int? = null
) :
    PagingSource<Int, Product>() {
    private val userFavourites = userPrefs.getLoggedUser().favouriteProductsId
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {
            val page = params.key ?: 0
            val pageSize = params.loadSize
            val response = productRepository.fetchProducts(
                page,
                pageSize,
                productName,
                categoryId,
                supermarketIds,
                onSale,
                alphabeticSort,
                priceSort
            )
            val data = response.content
            val dbCount = productRepository.countAllProductsFromDB()
            Log.i("Count", dbCount.toString())

            /*if (page == 0 && dbCount > 0) {
                productRepository.clearAllProductsFromDB()
                priceHistoryRepository.clearAll()
            }*/

            val priceHistoryEntityList = mutableListOf<PriceHistoryEntity>()
            data.forEach { product ->
                product.priceHistories
                    .forEach { priceHistory ->
                        priceHistoryEntityList.add(priceHistory.toPriceHistoryEntity(product.id))
                    }
            }

            priceHistoryRepository.insertPrices(priceHistoryEntityList)
            productRepository.insertProductsInDB(data.map {
                it.isFavourite = userFavourites?.contains(it.id) == true
                it.toProductEntity()
            })


            LoadResult.Page(
                data = data,
                prevKey = if (response.first) null else page - 1,
                nextKey = if (response.last) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}