package edu.miguelangelmoreno.shoppinglistapp.utils.mappers

import edu.miguelangelmoreno.shoppinglistapp.entity.PriceHistoryEntity
import edu.miguelangelmoreno.shoppinglistapp.model.PriceHistory

fun PriceHistory.toPriceHistoryEntity(productId : String) : PriceHistoryEntity {
    return PriceHistoryEntity(
        productId = productId,
        date = date,
        price = price,
        bulkPrice = bulkPrice,
        salePrice = salePrice
    )
}

fun PriceHistoryEntity.toPriceHistory(productId : String) : PriceHistory {
    return PriceHistory(
        productId = productId,
        date = date,
        price = price,
        bulkPrice = bulkPrice,
        salePrice = salePrice
    )
}