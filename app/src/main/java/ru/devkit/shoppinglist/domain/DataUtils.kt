package ru.devkit.shoppinglist.domain

import ru.devkit.shoppinglist.data.model.Product
import ru.devkit.shoppinglist.data.model.ProductDataModel

fun ProductDataModel.mapToProduct(): Product {
    return Product(
        name = title,
        checked = completed,
        index = position
    )
}

fun Product.mapToDataModel(): ProductDataModel {
    return ProductDataModel(
        title = name,
        completed = checked,
        position = index,
        selected = false
    )
}
