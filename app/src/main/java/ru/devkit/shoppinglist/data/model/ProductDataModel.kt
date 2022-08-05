package ru.devkit.shoppinglist.data.model

data class ProductDataModel constructor(
    val title: String,
    val completed: Boolean,
    val position: Int,
    var selected: Boolean
)