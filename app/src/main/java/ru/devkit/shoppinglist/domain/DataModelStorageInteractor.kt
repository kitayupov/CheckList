package ru.devkit.shoppinglist.domain

import androidx.annotation.WorkerThread
import ru.devkit.shoppinglist.data.model.ListItemDataModel
import ru.devkit.shoppinglist.data.repository.ProductsRepository

class DataModelStorageInteractor(
    private val repository: ProductsRepository
) {
    @WorkerThread
    suspend fun addItem(model: ListItemDataModel) {
        repository.addItem(model.mapToProduct())
    }

    @WorkerThread
    suspend fun updateItem(model: ListItemDataModel) {
        repository.updateItem(model.mapToProduct())
    }

    @WorkerThread
    suspend fun removeItem(model: ListItemDataModel) {
        repository.removeItem(model.mapToProduct())
    }

    @WorkerThread
    suspend fun getItems(): List<ListItemDataModel> {
        return repository.getItems().map { it.mapToDataModel() }
    }

    @WorkerThread
    suspend fun clearData() {
        return repository.clearData()
    }

    @WorkerThread
    suspend fun removeItemWithName(name: String) {
        return repository.removeItemWithName(name)
    }
}