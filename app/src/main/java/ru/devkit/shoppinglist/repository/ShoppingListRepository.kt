package ru.devkit.shoppinglist.repository

import ru.devkit.shoppinglist.model.ShoppingListItemModel

class ShoppingListRepository {

    private val list = mutableListOf(
        ShoppingListItemModel("potato"),
        ShoppingListItemModel("tomato"),
        ShoppingListItemModel("soup"),
        ShoppingListItemModel("banana"),
        ShoppingListItemModel("milk"),
        ShoppingListItemModel("bread"),
    )

    fun getItems(): List<ShoppingListItemModel> {
        return list
    }

    fun addItem(item: ShoppingListItemModel) {
        list.add(item)
    }

    fun removeItem(item: ShoppingListItemModel) {
        list.remove(item)
    }
}