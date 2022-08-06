package ru.devkit.checklist.ui.presentation

import ru.devkit.checklist.data.model.ProductDataModel
import ru.devkit.checklist.ui.model.ListItemModel

object CheckListContract {

    interface MvpView {
        fun showItems(list: List<ListItemModel>)
        fun setSelectionMode(checked: Boolean)
        fun showSelectedCount(value: Int)
    }

    interface MvpPresenter {
        fun attachView(view: MvpView)
        fun detachView()

        // update storage
        fun createItem(item: ProductDataModel)
        fun updateItem(item: ProductDataModel)
        fun removeItem(item: ProductDataModel)
        fun clearData()

        // appearance
        fun expandCompleted(checked: Boolean)

        // sort
        fun setSortRanking()
        fun setSortDefault()

        // selection
        fun selectItem(item: ProductDataModel)
        fun clearSelected()
        fun removeSelected()
        fun selectAll()
        fun checkSelected()
        fun uncheckSelected()
    }
}