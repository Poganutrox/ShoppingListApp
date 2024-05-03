package edu.miguelangelmoreno.shoppinglistapp.utils.mappers

import edu.miguelangelmoreno.shoppinglistapp.entity.ProductEntity
import edu.miguelangelmoreno.shoppinglistapp.model.PriceHistory
import edu.miguelangelmoreno.shoppinglistapp.model.Product
import java.math.BigDecimal

fun ProductEntity.toProduct(): Product {
    return Product(
        id = id,
        categoryId = categoryId,
        supermarketId = supermarketId,
        name = name,
        image = image,
        onSale = onSale,
        isFavourite = isFavourite,
        priceVariation = 0.0,
        timesInList = 0,
        priceHistories = emptyList()
    )
}

fun Product.toProductEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        categoryId = categoryId,
        supermarketId = supermarketId,
        name = name,
        image = image,
        onSale = onSale,
        isFavourite = isFavourite
    )
}