package edu.miguelangelmoreno.shoppinglistapp.data.repository

import edu.miguelangelmoreno.shoppinglistapp.data.service.ShoppingListDAO
import edu.miguelangelmoreno.shoppinglistapp.entity.PriceHistoryEntity
import javax.inject.Inject

class PriceHistoryRepository @Inject constructor(
    private val shoppingDAO : ShoppingListDAO) {
    suspend fun getPriceByProductId(productId: String) = shoppingDAO.getPriceByProductId(productId)

    suspend fun insertPrices(prices: List<PriceHistoryEntity>){
        shoppingDAO.insertPrices(prices)
    }

    suspend fun clearAll(){
        shoppingDAO.clearAllPrices()
    }

    suspend fun countAll() = shoppingDAO.countAllPrices()
}