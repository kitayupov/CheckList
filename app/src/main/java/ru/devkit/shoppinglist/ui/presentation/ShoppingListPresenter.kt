package ru.devkit.shoppinglist.ui.presentation

import ru.devkit.shoppinglist.data.model.ListItemDataModel
import ru.devkit.shoppinglist.data.repository.ShoppingListRepository
import ru.devkit.shoppinglist.ui.model.ListItemUiModel

class ShoppingListPresenter(
    private val repository: ShoppingListRepository
) : ShoppingListContract.MvpPresenter {

    private var view: ShoppingListContract.MvpView? = null

    override fun attachView(view: ShoppingListContract.MvpView) {
        this.view = view
        updateItems()
    }

    override fun detachView() {
        this.view = null
    }

    override fun addItem(item: ListItemDataModel) {
        repository.addItem(item)
        updateItems()
    }

    override fun updateItem(item: ListItemDataModel) {
        repository.updateItem(item)
        updateItems()
    }

    override fun removeItem(item: ListItemDataModel) {
        repository.removeItem(item)
        updateItems()
    }

    private fun updateItems() {
        view?.apply {
            val elements = repository.getItems().map { ListItemUiModel.Element(it) }

            val unchecked = elements.filterNot { it.data.checked }
            val checked = elements.filter { it.data.checked }

            val items = mutableListOf<ListItemUiModel>()
            items.addAll(unchecked)

            if (checked.isNotEmpty()) {
                items.add(ListItemUiModel.Divider)
                items.addAll(checked)
            }

            showItems(items)
        }
    }
}