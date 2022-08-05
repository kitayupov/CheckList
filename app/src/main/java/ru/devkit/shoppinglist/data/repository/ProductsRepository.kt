package ru.devkit.shoppinglist.data.repository

import ru.devkit.shoppinglist.data.model.Product
import ru.devkit.shoppinglist.data.source.DataSource

class ProductsRepository(
    private val dataSource: DataSource<Product, String>
) {

    fun getItems(): List<Product> {
        return dataSource.getItems()
    }

    fun addItem(elem: Product) {
        dataSource.create(elem)
    }

    fun updateItem(elem: Product) {
        dataSource.update(elem)
    }

    fun removeItem(elem: Product) {
        dataSource.delete(elem)
    }

    fun clearData() {
        dataSource.clear()
    }

    fun removeItemWithName(name: String) {
        dataSource.getItemWithKey(name)?.let(dataSource::delete)
    }
}