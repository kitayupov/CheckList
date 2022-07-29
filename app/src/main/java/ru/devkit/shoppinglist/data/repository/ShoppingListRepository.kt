package ru.devkit.shoppinglist.data.repository

import ru.devkit.shoppinglist.data.model.ListItemDataModel

class ShoppingListRepository {

    private val list = mutableListOf(
        ListItemDataModel("potato"),
        ListItemDataModel("tomato"),
        ListItemDataModel("soup"),
        ListItemDataModel("banana"),
        ListItemDataModel("milk"),
        ListItemDataModel("bread"),
    )

    fun getItems(): List<ListItemDataModel> {
        return list
    }

    fun addItem(item: ListItemDataModel) {
        list.add(item)
    }

    fun updateItem(item: ListItemDataModel) {
        val index = list.indexOfFirst { it.title == item.title }
        if (index != -1) {
            list[index] = item
        } else {
            list.add(item)
        }
    }

    fun removeItem(item: ListItemDataModel) {
        list.remove(item)
    }
}