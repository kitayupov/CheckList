package ru.devkit.shoppinglist.data.repository

import ru.devkit.shoppinglist.data.model.ListItemDataModel
import ru.devkit.shoppinglist.data.source.DataSource

class ShoppingListRepository(
    private val dataSource: DataSource<ListItemDataModel>
) {

    fun getItems(): List<ListItemDataModel> {
        return dataSource.getItems()
    }

    fun addItem(elem: ListItemDataModel) {
        dataSource.create(elem)
    }

    fun updateItem(elem: ListItemDataModel) {
        dataSource.update(elem)
    }

    fun removeItem(elem: ListItemDataModel) {
        dataSource.delete(elem)
    }
}