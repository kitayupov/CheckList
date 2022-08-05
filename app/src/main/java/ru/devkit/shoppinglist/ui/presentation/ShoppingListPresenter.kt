package ru.devkit.shoppinglist.ui.presentation

import kotlinx.coroutines.*
import ru.devkit.shoppinglist.data.model.ProductDataModel
import ru.devkit.shoppinglist.data.preferences.PreferencesProvider
import ru.devkit.shoppinglist.domain.DataModelStorageInteractor
import ru.devkit.shoppinglist.ui.model.ListItemModel

class ShoppingListPresenter(
    private val interactor: DataModelStorageInteractor,
    private val preferences: PreferencesProvider
) : ShoppingListContract.MvpPresenter {

    private var view: ShoppingListContract.MvpView? = null

    private val mainScope = CoroutineScope(Dispatchers.Main)

    private val cachedList = mutableListOf<ProductDataModel>()
    private val selectedKeys = mutableListOf<String>()

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

    override fun addItem(item: ProductDataModel) {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                interactor.addItem(item)
            }
            updateItems()
        }
    }

    override fun updateItem(item: ProductDataModel) {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                interactor.updateItem(item)
            }
            updateItems()
        }
    }

    override fun removeItem(item: ProductDataModel) {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                interactor.removeItem(item)
            }
            updateItems()
        }
    }

    override fun expandArchived(checked: Boolean) {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                preferences.putBoolean(PreferencesProvider.EXPAND_ARCHIVED_KEY, checked)
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

    override fun selectItem(item: ProductDataModel) {
        mainScope.launch {
            if (selectedKeys.isEmpty()) {
                view?.setSelectionMode(true)
            }
            val element = item.title
            if (selectedKeys.contains(element)) {
                selectedKeys.remove(element)
            } else {
                selectedKeys.add(element)
            }
            updateItems()
        }
    }

    override fun clearSelected() {
        mainScope.launch {
            selectedKeys.clear()
            updateItems()
        }
    }

    override fun removeSelected() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                forEachSelected {
                    interactor.removeItem(it)
                }
                selectedKeys.clear()
            }
            updateItems()
        }
    }

    override fun selectAll() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                selectedKeys.clear()
                cachedList.forEach {
                    selectedKeys.add(it.title)
                }
            }
            updateItems()
        }
    }

    override fun checkSelected() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                forEachSelected {
                    updateItem(it.copy(completed = true))
                }
            }
            updateItems()
        }
    }

    override fun uncheckSelected() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                forEachSelected {
                    updateItem(it.copy(completed = false))
                }
            }
            updateItems()
        }
    }

    private inline fun forEachSelected(action: (ProductDataModel) -> Unit) {
        selectedKeys.forEach { name ->
            cachedList.find { it.title == name }
                ?.let { action.invoke(it) }
        }
    }

    private suspend fun updateItems() {
        view?.apply {
            val elements = withContext(Dispatchers.IO) {
                interactor.getItems()
                    .also {
                        cachedList.clear()
                        cachedList.addAll(it)
                    }
                    .map { ListItemModel.Element(it) }
            }

            val unchecked = elements.filterNot { it.data.completed }
            val checked = elements.filter { it.data.completed }

            val items = mutableListOf<ListItemModel>()
            items.addAll(unchecked)

            if (checked.isNotEmpty()) {
                val expanded = preferences.getBoolean(PreferencesProvider.EXPAND_ARCHIVED_KEY, true)
                items.add(ListItemModel.Divider(expanded))
                if (expanded) {
                    items.addAll(checked)
                }
            }

            if (selectedKeys.isNotEmpty()) {
                items.forEach {
                    if (it is ListItemModel.Element) {
                        val model = it.data
                        model.selected = selectedKeys.contains(model.title)
                    }
                }
                view?.showSelectedCount(selectedKeys.size)
            } else {
                view?.setSelectionMode(false)
            }

            showItems(items)
        }
    }
}