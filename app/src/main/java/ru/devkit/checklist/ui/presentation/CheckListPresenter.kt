package ru.devkit.checklist.ui.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import ru.devkit.checklist.data.model.ProductDataModel
import ru.devkit.checklist.data.preferences.PreferencesProvider
import ru.devkit.checklist.domain.DataModelStorageInteractor
import ru.devkit.checklist.domain.SortType
import ru.devkit.checklist.ui.model.ListItemModel

class CheckListPresenter(
    private val interactor: DataModelStorageInteractor,
    private val preferences: PreferencesProvider
) : CheckListContract.MvpPresenter, CoroutinePresenter() {

    private var view: CheckListContract.MvpView? = null

    private val cachedList = mutableListOf<ProductDataModel>()
    private val selectedKeys = mutableListOf<String>()

    private var expandCompleted = preferences.expandCompleted
    private var sortType = preferences.sortType

    override fun attachView(view: CheckListContract.MvpView) {
        this.view = view
        launch {
            updateItems()
        }
    }

    override fun detachView() {
        this.view = null
        cancel()
    }

    override fun createItem(name: String) = update {
        // check if item already exists
        cachedList.find { it.title == name }?.let { data ->
            if (data.completed.not()) {
                // do nothing
                view?.showMessage("The item \'$name\' already exists")
            } else {
                // just return to the list
                interactor.updateItem(data.copy(completed = false))
                view?.showMessage("The item \'$name\' returned")
            }
        } ?: kotlin.run {
            interactor.addItem(ProductDataModel(name))
            view?.showMessage("The item \'$name\' added")
        }
    }

    override fun renameItem(oldName: String, newName: String) = update {
        cachedList.find { it.title == oldName }?.let { data ->
            val update = data.copy(title = newName)
            interactor.removeItem(data)
            interactor.addItem(update)
        }
    }

    override fun switchChecked(name: String) = update {
        cachedList.find { it.title == name }?.let { data ->
            val update = data.copy(
                completed = data.completed.not(),
                lastUpdated = System.currentTimeMillis(),
                ranking = data.ranking + if (data.completed.not()) 0 else 1
            )
            interactor.updateItem(update)
        }
    }

    override fun removeItem(name: String) = update {
        cachedList.find { it.title == name }?.let {
            interactor.removeItem(it)
        }
    }

    override fun clearData() = update {
        interactor.clearData()
    }

    override fun expandCompleted(checked: Boolean) = update {
        if (expandCompleted == checked) return@update
        expandCompleted = checked
        preferences.expandCompleted = checked
    }

    override fun setSortRanking() = setSortType(SortType.RANKING)
    override fun setSortDefault() = setSortType(SortType.DEFAULT)
    override fun setSortName() = setSortType(SortType.NAME)

    private fun setSortType(sortType: SortType) = update {
        if (this@CheckListPresenter.sortType == sortType) return@update
        this@CheckListPresenter.sortType = sortType
        preferences.sortType = sortType
    }

    override fun switchSelected(name: String) {
        launch {
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

    override fun clearSelected() = update {
        selectedKeys.clear()
    }

    override fun removeSelected() = update {
        forEachSelected {
            interactor.removeItem(it)
        }
        selectedKeys.clear()
    }

    override fun selectAll() = update {
        selectedKeys.clear()
        cachedList.forEach {
            selectedKeys.add(it.title)
        }
    }

    override fun checkSelected() = update {
        forEachSelected {
            interactor.updateItem(it.copy(completed = true))
        }
    }

    override fun uncheckSelected() = update {
        forEachSelected {
            interactor.updateItem(it.copy(completed = false))
        }
    }

    private inline fun forEachSelected(action: (ProductDataModel) -> Unit) {
        selectedKeys.forEach { name ->
            cachedList.find { it.title == name }
                ?.let { action.invoke(it) }
        }
    }

    private fun update(modification: suspend CoroutineScope.() -> Unit) {
        launch {
            modification()
            updateItems()
        }
    }

    private suspend fun updateItems() {
        view?.apply {
            val elements = interactor.getItems()
                .also {
                    cachedList.clear()
                    cachedList.addAll(it)
                }
                .map { ListItemModel.Element(it) }

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
                items.add(ListItemModel.Divider(checked.size, expandCompleted))
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