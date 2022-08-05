package ru.devkit.shoppinglist.ui.presentation

import kotlinx.coroutines.*
import ru.devkit.shoppinglist.data.model.ListItemDataModel
import ru.devkit.shoppinglist.data.preferences.PreferencesProvider
import ru.devkit.shoppinglist.domain.DataModelStorageInteractor
import ru.devkit.shoppinglist.ui.model.ListItemUiModel

class ShoppingListPresenter(
    private val interactor: DataModelStorageInteractor,
    private val preferences: PreferencesProvider
) : ShoppingListContract.MvpPresenter {

    private var view: ShoppingListContract.MvpView? = null

    private val mainScope = CoroutineScope(Dispatchers.Main)

    private val cached = mutableListOf<ListItemDataModel>()
    private val selected = mutableListOf<String>()

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

    override fun selectItem(item: ListItemDataModel) {
        mainScope.launch {
            if (selected.isEmpty()) {
                view?.setSelectionMode(true)
            }
            val element = item.title
            if (selected.contains(element)) {
                selected.remove(element)
            } else {
                selected.add(element)
            }
            updateItems()
        }
    }

    override fun clearSelected() {
        mainScope.launch {
            selected.clear()
            updateItems()
        }
    }

    override fun removeSelected() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                forEachSelected {
                    interactor.removeItem(it)
                }
                selected.clear()
            }
            updateItems()
        }
    }

    override fun selectAll() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                selected.clear()
                cached.forEach {
                    selected.add(it.title)
                }
            }
            updateItems()
        }
    }

    override fun checkSelected() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                forEachSelected {
                    updateItem(it.copy(checked = true))
                }
            }
            updateItems()
        }
    }

    override fun uncheckSelected() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                forEachSelected {
                    updateItem(it.copy(checked = false))
                }
            }
            updateItems()
        }
    }

    private inline fun forEachSelected(action: (ListItemDataModel) -> Unit) {
        selected.forEach { name ->
            cached.find { it.title == name }
                ?.let { action.invoke(it) }
        }
    }

    private suspend fun updateItems() {
        view?.apply {
            val elements = withContext(Dispatchers.IO) {
                interactor.getItems()
                    .also {
                        cached.clear()
                        cached.addAll(it)
                    }
                    .map { ListItemUiModel.Element(it) }
            }

            val unchecked = elements.filterNot { it.data.checked }
            val checked = elements.filter { it.data.checked }

            val items = mutableListOf<ListItemUiModel>()
            items.addAll(unchecked)

            if (checked.isNotEmpty()) {
                val expanded = preferences.getBoolean(PreferencesProvider.EXPAND_ARCHIVED_KEY, true)
                items.add(ListItemUiModel.Divider(expanded))
                if (expanded) {
                    items.addAll(checked)
                }
            }

            if (selected.isNotEmpty()) {
                items.forEach {
                    if (it is ListItemUiModel.Element) {
                        val model = it.data
                        model.selected = selected.contains(model.title)
                    }
                }
                view?.showSelectedCount(selected.size)
            } else {
                view?.setSelectionMode(false)
            }

            showItems(items)
        }
    }
}