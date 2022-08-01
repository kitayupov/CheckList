package ru.devkit.shoppinglist.domain

import ru.devkit.shoppinglist.data.model.ListItemDataModel
import ru.devkit.shoppinglist.data.model.Product

fun ListItemDataModel.mapToProduct(): Product {
    return Product(title, checked)
}

fun Product.mapToDataModel(): ListItemDataModel {
    return ListItemDataModel(name, checked)
}
