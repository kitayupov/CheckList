package ru.devkit.checklist.domain

import ru.devkit.checklist.data.model.Product
import ru.devkit.checklist.data.model.ProductDataModel

fun ProductDataModel.mapToProduct(): Product {
    return Product(
        name = title,
        checked = completed,
        lastUpdated = lastUpdated,
        ranking = ranking
    )
}

fun Product.mapToDataModel(): ProductDataModel {
    return ProductDataModel(
        title = name,
        completed = checked,
        lastUpdated = lastUpdated,
        ranking = ranking,
        selected = false
    )
}
