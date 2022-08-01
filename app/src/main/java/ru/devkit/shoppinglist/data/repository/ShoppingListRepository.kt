package ru.devkit.shoppinglist.data.repository

import ru.devkit.shoppinglist.data.model.ListItemDataModel
import ru.devkit.shoppinglist.data.source.DataSource

class ShoppingListRepository(
    private val dataSource: DataSource<ListItemDataModel>
) {

    fun getItems(): List<ListItemDataModel> {
        return dataSource.getItems()
    }

    fun addItem(item: ListItemDataModel) {
        dataSource.add(item)
    }

    fun updateItem(item: ListItemDataModel) {
        dataSource.update(item)

    }

    fun removeItem(item: ListItemDataModel) {
        dataSource.remove(item)
    }
}