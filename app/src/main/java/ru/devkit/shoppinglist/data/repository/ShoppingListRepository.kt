package ru.devkit.shoppinglist.data.repository

import ru.devkit.shoppinglist.data.model.ShoppingListItemModel

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