package edu.miguelangelmoreno.shoppinglistapp.utils.mappers

import edu.miguelangelmoreno.shoppinglistapp.entity.ProductEntity
import edu.miguelangelmoreno.shoppinglistapp.model.PriceHistory
import edu.miguelangelmoreno.shoppinglistapp.model.Product

fun ProductEntity.toProduct(): Product {
    return Product(
        id = id,
        categoryId = categoryId,
        supermarketId = supermarketId,
        name = name,
        image = image,
        onSale = onSale,
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
        onSale = onSale
    )
}