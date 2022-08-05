package ru.devkit.shoppinglist.domain

import ru.devkit.shoppinglist.data.model.ProductDataModel
import ru.devkit.shoppinglist.data.model.Product

fun ProductDataModel.mapToProduct(): Product {
    return Product(title, completed)
}

fun Product.mapToDataModel(): ProductDataModel {
    return ProductDataModel(name, checked)
}
