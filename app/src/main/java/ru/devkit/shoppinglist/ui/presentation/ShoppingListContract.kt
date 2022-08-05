package ru.devkit.shoppinglist.ui.presentation

import ru.devkit.shoppinglist.data.model.ProductDataModel
import ru.devkit.shoppinglist.ui.model.ListItemUiModel

object ShoppingListContract {

    interface MvpView {
        fun showItems(list: List<ListItemUiModel>)
        fun setSelectionMode(checked: Boolean)
        fun showSelectedCount(value: Int)
    }

    interface MvpPresenter {
        fun attachView(view: MvpView)
        fun detachView()

        // update storage
        fun addItem(item: ProductDataModel)
        fun updateItem(item: ProductDataModel)
        fun removeItem(item: ProductDataModel)
        fun clearData()

        // appearance
        fun expandArchived(checked: Boolean)

        // selection
        fun selectItem(item: ProductDataModel)
        fun clearSelected()
        fun removeSelected()
        fun selectAll()
        fun checkSelected()
        fun uncheckSelected()
    }
}