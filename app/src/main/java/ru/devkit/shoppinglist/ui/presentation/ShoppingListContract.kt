package ru.devkit.shoppinglist.ui.presentation

import ru.devkit.shoppinglist.data.model.ListItemDataModel
import ru.devkit.shoppinglist.ui.model.ListItemUiModel

object ShoppingListContract {

    interface MvpView {
        fun showItems(list: List<ListItemUiModel>)
        fun selectionMode(checked: Boolean)
    }

    interface MvpPresenter {
        fun attachView(view: MvpView)
        fun detachView()

        // update storage
        fun addItem(item: ListItemDataModel)
        fun updateItem(item: ListItemDataModel)
        fun removeItem(item: ListItemDataModel)
        fun clearData()

        // appearance
        fun expandArchived(checked: Boolean)

        // selection
        fun selectItem(item: ListItemDataModel)
        fun clearSelected()
    }
}