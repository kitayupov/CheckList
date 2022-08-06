package ru.devkit.checklist.ui.presentation

import kotlinx.coroutines.*
import ru.devkit.checklist.data.model.ProductDataModel
import ru.devkit.checklist.data.preferences.PreferencesProvider
import ru.devkit.checklist.domain.DataModelStorageInteractor
import ru.devkit.checklist.domain.SortType
import ru.devkit.checklist.ui.model.ListItemModel

class CheckListPresenter(
    private val interactor: DataModelStorageInteractor,
    private val preferences: PreferencesProvider
) : CheckListContract.MvpPresenter {

    private var view: CheckListContract.MvpView? = null

    private val mainScope = CoroutineScope(Dispatchers.Main)

    private val cachedList = mutableListOf<ProductDataModel>()
    private val selectedKeys = mutableListOf<String>()

    private var sortType = readSortType()

    override fun attachView(view: CheckListContract.MvpView) {
        this.view = view
        mainScope.launch {
            updateItems()
        }
    }

    override fun detachView() {
        this.view = null
        mainScope.cancel()
    }

    override fun createItem(item: ProductDataModel) {
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

    override fun expandCompleted(checked: Boolean) {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                preferences.putBoolean(PreferencesProvider.EXPAND_COMPLETED_KEY, checked)
            }
            updateItems()
        }
    }

    override fun setSortRanking() = setSortType(SortType.RANKING)

    override fun setSortDefault() = setSortType(SortType.DEFAULT)

    private fun setSortType(sortType: SortType) {
        this.sortType = sortType
        mainScope.launch {
            withContext(Dispatchers.IO) {
                writeSortType(sortType)
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

    private fun readSortType(): SortType {
        val value = preferences.getString(PreferencesProvider.SORT_TYPE_KEY, SortType.DEFAULT.name)
        return SortType.fromString(value)
    }

    private fun writeSortType(sortType: SortType) {
        preferences.putString(PreferencesProvider.SORT_TYPE_KEY, sortType.name)
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

            // separate checked and unchecked
            val unchecked = elements
                .filterNot { it.data.completed }
                .run {
                    when (sortType) {
                        SortType.DEFAULT -> sortedBy { it.data.lastUpdated }
                        SortType.RANKING -> sortedByDescending { it.data.ranking }
                    }
                }
            val checked = elements
                .filter { it.data.completed }
                .run {
                    when (sortType) {
                        SortType.DEFAULT -> sortedByDescending { it.data.lastUpdated }
                        SortType.RANKING -> sortedBy { it.data.ranking }
                    }
                }

            val items = mutableListOf<ListItemModel>()
            items.addAll(unchecked)

            // configure completed area
            if (checked.isNotEmpty()) {
                val expanded = preferences.getBoolean(PreferencesProvider.EXPAND_COMPLETED_KEY, true)
                items.add(ListItemModel.Divider(expanded))
                if (expanded) {
                    items.addAll(checked)
                }
            }

            // update selections
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