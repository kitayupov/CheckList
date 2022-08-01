package ru.devkit.shoppinglist.data.source

import ru.devkit.shoppinglist.data.model.ListItemDataModel

class TestShoppingListDataSource : DataSource<ListItemDataModel> {

    private val list = mutableListOf(
        ListItemDataModel("potato"),
        ListItemDataModel("tomato"),
        ListItemDataModel("soup"),
        ListItemDataModel("banana"),
        ListItemDataModel("milk"),
        ListItemDataModel("bread"),
    )

    override fun getItems(): List<ListItemDataModel> {
        return list
    }

    override fun add(item: ListItemDataModel) {
        list.add(item)
    }

    override fun update(item: ListItemDataModel) {
        val index = list.indexOfFirst { it.title == item.title }
        if (index != -1) {
            list[index] = item
        } else {
            list.add(item)
        }
    }

    override fun remove(item: ListItemDataModel) {
        list.remove(item)
    }
}