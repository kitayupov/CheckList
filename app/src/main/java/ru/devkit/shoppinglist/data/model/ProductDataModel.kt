package ru.devkit.shoppinglist.data.model

data class ProductDataModel(
    val title: String,
    val completed: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis(),
    var selected: Boolean = false
)