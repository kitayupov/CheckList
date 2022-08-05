package ru.devkit.shoppinglist.domain

import ru.devkit.shoppinglist.data.model.Product
import ru.devkit.shoppinglist.data.model.ProductDataModel

fun ProductDataModel.mapToProduct(): Product {
    return Product(
        name = title,
        checked = completed,
        lastUpdated = lastUpdated
    )
}

fun Product.mapToDataModel(): ProductDataModel {
    return ProductDataModel(
        title = name,
        completed = checked,
        lastUpdated = lastUpdated,
        selected = false
    )
}
