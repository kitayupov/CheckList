package ru.devkit.shoppinglist.data.source

import ru.devkit.shoppinglist.data.model.Product

class TestShoppingListDataSource : DataSource<Product> {

    private val list = mutableListOf(
        Product("potato", false),
        Product("tomato", false),
        Product("soup", false),
        Product("banana", false),
        Product("milk", false),
        Product("bread", false),
    )

    override fun getItems(): List<Product> {
        return list
    }

    override fun create(elem: Product) {
        list.add(elem)
    }

    override fun update(elem: Product) {
        val index = list.indexOfFirst { it.name == elem.name }
        if (index != -1) {
            list[index] = elem
        } else {
            list.add(elem)
        }
    }

    override fun delete(elem: Product) {
        list.remove(elem)
    }
}