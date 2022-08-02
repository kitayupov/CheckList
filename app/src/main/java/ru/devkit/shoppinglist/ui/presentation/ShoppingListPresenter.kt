package ru.devkit.shoppinglist.ui.presentation

import kotlinx.coroutines.*
import ru.devkit.shoppinglist.data.model.ListItemDataModel
import ru.devkit.shoppinglist.domain.DataModelStorageInteractor
import ru.devkit.shoppinglist.ui.model.ListItemUiModel

class ShoppingListPresenter(
    private val interactor: DataModelStorageInteractor
) : ShoppingListContract.MvpPresenter {

    private var view: ShoppingListContract.MvpView? = null

    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun attachView(view: ShoppingListContract.MvpView) {
        this.view = view
        mainScope.launch {
            updateItems()
        }
    }

    override fun detachView() {
        this.view = null
        mainScope.cancel()
    }

    override fun addItem(item: ListItemDataModel) {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                interactor.addItem(item)
            }
            updateItems()
        }
    }

    override fun updateItem(item: ListItemDataModel) {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                interactor.updateItem(item)
            }
            updateItems()
        }
    }

    override fun removeItem(item: ListItemDataModel) {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                interactor.removeItem(item)
            }
            updateItems()
        }
    }

    override fun clearData() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                interactor.clearData()
            }
            updateItems()
        }
    }

    private suspend fun updateItems() {
        view?.apply {
            val elements = withContext(Dispatchers.IO) {
                interactor.getItems()
                    .map { ListItemUiModel.Element(it) }
            }

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