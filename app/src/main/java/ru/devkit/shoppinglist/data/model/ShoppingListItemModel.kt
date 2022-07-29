package ru.devkit.shoppinglist.data.model

data class ShoppingListItemModel(
    val title: String,
    var checked: Boolean = false
)