package ru.devkit.checklist.domain

import androidx.annotation.WorkerThread
import ru.devkit.checklist.data.model.ProductDataModel
import ru.devkit.checklist.data.repository.ProductsRepository

class DataModelStorageInteractor(
    private val repository: ProductsRepository
) {
    @WorkerThread
    suspend fun addItem(model: ProductDataModel) {
        repository.addItem(model.mapToProduct())
    }

    @WorkerThread
    suspend fun updateItem(model: ProductDataModel) {
        repository.updateItem(model.mapToProduct())
    }

    @WorkerThread
    suspend fun removeItem(model: ProductDataModel) {
        repository.removeItem(model.mapToProduct())
    }

    @WorkerThread
    suspend fun getItems(): List<ProductDataModel> {
        return repository.getItems().map { it.mapToDataModel() }
    }

    @WorkerThread
    suspend fun clearData() {
        return repository.clearData()
    }
}