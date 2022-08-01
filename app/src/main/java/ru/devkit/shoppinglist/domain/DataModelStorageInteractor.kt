package ru.devkit.shoppinglist.domain

import ru.devkit.shoppinglist.data.model.ListItemDataModel
import ru.devkit.shoppinglist.data.repository.ProductsRepository

class DataModelStorageInteractor(
    private val repository: ProductsRepository
) {
    fun addItem(model: ListItemDataModel) {
        repository.addItem(model.mapToProduct())
    }

    fun updateItem(model: ListItemDataModel) {
        repository.updateItem(model.mapToProduct())
    }

    fun removeItem(model: ListItemDataModel) {
        repository.removeItem(model.mapToProduct())
    }

    fun getItems(): List<ListItemDataModel> {
        return repository.getItems().map { it.mapToDataModel() }
    }
}