package ru.devkit.checklist.data.repository

import ru.devkit.checklist.data.model.Product
import ru.devkit.checklist.data.source.DataSource

class ProductsRepository(
    private val dataSource: DataSource<Product>
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
}