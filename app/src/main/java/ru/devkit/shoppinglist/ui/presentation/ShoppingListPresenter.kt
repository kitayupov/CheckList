package ru.devkit.shoppinglist.ui.presentation

import ru.devkit.shoppinglist.data.model.ListItemDataModel
import ru.devkit.shoppinglist.data.repository.ProductsRepository
import ru.devkit.shoppinglist.domain.DataModelStorageInteractor
import ru.devkit.shoppinglist.ui.model.ListItemUiModel

class ShoppingListPresenter(
    private val interactor: DataModelStorageInteractor
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
        interactor.addItem(item)
        updateItems()
    }

    override fun updateItem(item: ListItemDataModel) {
        interactor.updateItem(item)
        updateItems()
    }

    override fun removeItem(item: ListItemDataModel) {
        interactor.removeItem(item)
        updateItems()
    }

    private fun updateItems() {
        view?.apply {
            val elements = interactor.getItems().map { ListItemUiModel.Element(it) }

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