package ru.devkit.shoppinglist.data.model

data class ProductDataModel(
    val title: String,
    val completed: Boolean = false,
    var selected: Boolean = false
)