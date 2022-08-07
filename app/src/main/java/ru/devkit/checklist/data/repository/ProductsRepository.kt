package ru.devkit.checklist.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.devkit.checklist.data.model.Product
import ru.devkit.checklist.data.source.DataSource

class ProductsRepository(
    private val dataSource: DataSource<Product>
) {

    suspend fun getItems(): List<Product> = withContext(Dispatchers.IO) {
        dataSource.getItems()
    }

    suspend fun addItem(elem: Product) = withContext(Dispatchers.IO) {
        dataSource.create(elem)
    }

    suspend fun updateItem(elem: Product) = withContext(Dispatchers.IO) {
        dataSource.update(elem)
    }

    suspend fun removeItem(elem: Product) = withContext(Dispatchers.IO) {
        dataSource.delete(elem)
    }

    suspend fun clearData() = withContext(Dispatchers.IO) {
        dataSource.clear()
    }
}