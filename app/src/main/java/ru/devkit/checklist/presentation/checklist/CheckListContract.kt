package ru.devkit.checklist.presentation.checklist

import ru.devkit.checklist.data.model.ProductDataModel

object CheckListContract {

    interface MvpView {
        fun showItems(actual: List<ProductDataModel>, completed: List<ProductDataModel>)
        fun setSelectionMode(checked: Boolean)
        fun scrollToPosition(position: Int)
        fun expandCompleted(checked: Boolean)
    }

    interface MvpPresenter {
        fun attachView(view: MvpView)
        fun detachView()

        // update storage
        fun createItem(name: String)
        fun renameItem(oldName: String, newName: String)
        fun switchChecked(name: String)
        fun removeItem(name: String)
        fun clearData()

        // reorder
        fun reorderResult(list: List<ProductDataModel>)

        // appearance
        fun expandCompleted(checked: Boolean)

        // sort
        fun setSortRanking()
        fun setSortDefault()
        fun setSortName()

        // selection
        fun switchSelected(name: String)
        fun clearSelected()
        fun removeSelected()
        fun selectAll()
        fun checkSelected()
        fun uncheckSelected()
    }
}