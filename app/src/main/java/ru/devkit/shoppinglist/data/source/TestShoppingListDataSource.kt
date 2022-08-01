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

    override fun create(elem: ListItemDataModel) {
        list.add(elem)
    }

    override fun update(elem: ListItemDataModel) {
        val index = list.indexOfFirst { it.title == elem.title }
        if (index != -1) {
            list[index] = elem
        } else {
            list.add(elem)
        }
    }

    override fun delete(elem: ListItemDataModel) {
        list.remove(elem)
    }
}