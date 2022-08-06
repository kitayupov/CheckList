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

    private var expandCompleted = preferences.expandCompleted
    private var sortType = preferences.sortType

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

    override fun createItem(name: String) {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                interactor.addItem(ProductDataModel(name))
            }
            updateItems()
        }
    }

    override fun switchChecked(name: String) {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                cachedList.find { it.title == name }?.let { data ->
                    val update = data.copy(
                        completed = data.completed.not(),
                        lastUpdated = System.currentTimeMillis(),
                        ranking = data.ranking + if (data.completed.not()) 0 else 1
                    )
                    updateItem(update)
                }
            }
        }
    }

    override fun removeItem(name: String) {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                cachedList.find { it.title == name }?.let {
                    interactor.removeItem(it)
                }
            }
            updateItems()
        }
    }

    override fun expandCompleted(checked: Boolean) {
        if (expandCompleted == checked) return
        expandCompleted = checked
        mainScope.launch {
            withContext(Dispatchers.IO) {
                preferences.expandCompleted = checked
            }
            updateItems()
        }
    }

    override fun setSortRanking() = setSortType(SortType.RANKING)

    override fun setSortDefault() = setSortType(SortType.DEFAULT)

    override fun setSortName() = setSortType(SortType.NAME)

    private fun setSortType(sortType: SortType) {
        if (this.sortType == sortType) return
        this.sortType = sortType
        mainScope.launch {
            withContext(Dispatchers.IO) {
                preferences.sortType = sortType
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

    override fun switchSelected(name: String) {
        mainScope.launch {
            if (selectedKeys.isEmpty()) {
                view?.setSelectionMode(true)
            }
            if (selectedKeys.contains(name)) {
                selectedKeys.remove(name)
            } else {
                selectedKeys.add(name)
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

    private fun updateItem(model: ProductDataModel) {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                interactor.updateItem(model)
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

            // separate checked and unchecked
            val unchecked = elements
                .filterNot { it.data.completed }
                .sortUnchecked()
            val checked = elements
                .filter { it.data.completed }
                .sortChecked()

            val items = mutableListOf<ListItemModel>()
            items.addAll(unchecked)

            // configure completed area
            if (checked.isNotEmpty()) {
                items.add(ListItemModel.Divider(expandCompleted))
                if (expandCompleted) {
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

    private fun List<ListItemModel.Element>.sortUnchecked() = when (sortType) {
        SortType.DEFAULT -> sortedBy { it.data.lastUpdated }
        SortType.RANKING -> sortedByDescending { it.data.ranking }
        SortType.NAME -> sortedBy { it.data.title }
    }

    private fun List<ListItemModel.Element>.sortChecked() = when (sortType) {
        SortType.DEFAULT -> sortedByDescending { it.data.lastUpdated }
        SortType.RANKING -> sortedByDescending { it.data.ranking }
        SortType.NAME -> sortedBy { it.data.title }
    }
}