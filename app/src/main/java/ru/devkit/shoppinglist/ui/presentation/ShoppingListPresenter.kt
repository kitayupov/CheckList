package ru.devkit.shoppinglist.ui.presentation

import ru.devkit.shoppinglist.data.model.ShoppingListItemModel
import ru.devkit.shoppinglist.data.repository.ShoppingListRepository

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

    override fun addItem(item: ShoppingListItemModel) {
        repository.addItem(item)
        updateItems()
    }

    override fun updateItem(item: ShoppingListItemModel) {
        repository.updateItem(item)
        updateItems()
    }

    override fun removeItem(item: ShoppingListItemModel) {
        repository.removeItem(item)
        updateItems()
    }

    private fun updateItems() {
        view?.apply {
            val items = repository.getItems()
            showItems(items)
        }
    }
}