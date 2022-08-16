package ru.devkit.checklist.presentation.checklist

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import ru.devkit.checklist.R
import ru.devkit.checklist.common.ResourceProvider
import ru.devkit.checklist.data.model.ProductDataModel
import ru.devkit.checklist.data.preferences.PreferencesProvider
import ru.devkit.checklist.domain.DataModelStorageInteractor
import ru.devkit.checklist.domain.SortType
import ru.devkit.checklist.presentation.actionmode.ActionModePresenter
import ru.devkit.checklist.presentation.common.BaseCoroutinePresenter
import ru.devkit.checklist.presentation.createitemaction.CreateItemActionPresenter
import ru.devkit.checklist.presentation.screenmessage.ScreenMessageInteractor
import ru.devkit.checklist.ui.model.ListItemModel

class CheckListPresenter(
    private val storageInteractor: DataModelStorageInteractor,
    private val messageInteractor: ScreenMessageInteractor,
    private val createItemActionPresenter: CreateItemActionPresenter,
    private val actionModePresenter: ActionModePresenter,
    private val preferences: PreferencesProvider,
    private val resources: ResourceProvider
) : CheckListContract.MvpPresenter, BaseCoroutinePresenter() {

    private var view: CheckListContract.MvpView? = null

    private var cachedSelectionMode = false

    private val cachedList = mutableListOf<ProductDataModel>()
    private val selectedKeys = mutableListOf<String>()

    private var focusedItemName: String? = null

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
        cachedSelectionMode = selectedKeys.isNotEmpty()
    }

    override fun createItem(name: String) = update {
        // check if item already exists
        focusedItemName = name
        cachedList.find { it.title == name }?.let { data ->
            if (data.completed.not()) {
                // do nothing
                messageInteractor.showMessage(resources.getString(R.string.message_item_exists, name))
            } else {
                // just return to the list
                storageInteractor.updateItem(data.copy(completed = false))
                messageInteractor.showMessage(resources.getString(R.string.message_item_returned, name))
            }
        } ?: kotlin.run {
            storageInteractor.addItem(ProductDataModel(name))
            messageInteractor.showMessage(resources.getString(R.string.message_item_added, name))
        }
    }

    override fun renameItem(oldName: String, newName: String) = update {
        cachedList.find { it.title == oldName }?.let { data ->
            val update = data.copy(title = newName)
            storageInteractor.removeItem(data)
            storageInteractor.addItem(update)
        }
    }

    override fun switchChecked(name: String) = update {
        cachedList.find { it.title == name }?.let { data ->
            val update = data.copy(
                completed = data.completed.not(),
                lastUpdated = System.currentTimeMillis(),
                ranking = data.ranking + if (data.completed.not()) 0 else 1
            )
            storageInteractor.updateItem(update)
        }
    }

    override fun removeItem(name: String) = update {
        cachedList.find { it.title == name }?.let {
            storageInteractor.removeItem(it)
        }
    }

    override fun clearData() = update {
        storageInteractor.clearData()
    }

    override fun reorderResult(list: List<ListItemModel>) {
        // store custom sort type
        // update storage
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

    override fun switchSelected(name: String) = update {
        if (selectedKeys.isEmpty()) {
            view?.setSelectionMode(true)
            createItemActionPresenter.hideView()
            actionModePresenter.setSelectionMode(true)
        }
        if (selectedKeys.contains(name)) {
            selectedKeys.remove(name)
        } else {
            selectedKeys.add(name)
        }
    }

    override fun clearSelected() = update {
        selectedKeys.clear()
    }

    override fun removeSelected() = update {
        forEachSelected {
            storageInteractor.removeItem(it)
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
            storageInteractor.updateItem(it.copy(completed = true))
        }
    }

    override fun uncheckSelected() = update {
        forEachSelected {
            storageInteractor.updateItem(it.copy(completed = false))
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
        val elements = storageInteractor.getItems()
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
            actionModePresenter.setSelectedCount(selectedKeys.size)
        } else {
            view?.setSelectionMode(false)
            createItemActionPresenter.showView()
            actionModePresenter.setSelectionMode(false)
        }

        // update selection mode after rotate
        if (cachedSelectionMode) {
            cachedSelectionMode = false
            view?.setSelectionMode(true)
            createItemActionPresenter.hideView()
            actionModePresenter.setSelectionMode(true)
            actionModePresenter.setSelectedCount(selectedKeys.size)
        }

        view?.showItems(items)

        focusedItemName?.let { name ->
            items.indexOfFirst { (it is ListItemModel.Element) && (it.data.title == name) }
                .takeIf { it >= 0 }
                ?.let { view?.scrollToPosition(it) }
            focusedItemName = null
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