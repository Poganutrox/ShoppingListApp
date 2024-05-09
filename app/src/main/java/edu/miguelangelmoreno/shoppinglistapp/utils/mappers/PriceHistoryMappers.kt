package edu.miguelangelmoreno.shoppinglistapp.utils.mappers

import edu.miguelangelmoreno.shoppinglistapp.entity.PriceHistoryEntity
import edu.miguelangelmoreno.shoppinglistapp.model.PriceHistory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val date = if (date.contains("-")) date.format(formatter) else date
    return PriceHistory(
        productId = productId,
        date = date,
        price = price,
        bulkPrice = bulkPrice,
        salePrice = salePrice
    )
}