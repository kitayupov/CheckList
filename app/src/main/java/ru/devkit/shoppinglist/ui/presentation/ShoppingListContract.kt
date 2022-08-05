package ru.devkit.shoppinglist.ui.presentation

import ru.devkit.shoppinglist.data.model.ProductDataModel
import ru.devkit.shoppinglist.ui.model.ListItemModel

object ShoppingListContract {

    interface MvpView {
        fun showItems(list: List<ListItemModel>)
        fun setSelectionMode(checked: Boolean)
        fun showSelectedCount(value: Int)
    }

    interface MvpPresenter {
        fun attachView(view: MvpView)
        fun detachView()

        // update storage
        fun createItem(name: String)
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