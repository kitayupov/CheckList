package ru.devkit.shoppinglist.ui.presentation

import ru.devkit.shoppinglist.data.model.ListItemDataModel
import ru.devkit.shoppinglist.ui.model.ListItemUiModel

object ShoppingListContract {

    interface MvpView {
        fun showItems(list: List<ListItemUiModel>)
    }

    interface MvpPresenter {
        fun attachView(view: MvpView)
        fun detachView()
        fun addItem(item: ListItemDataModel)
        fun updateItem(item: ListItemDataModel)
        fun removeItem(item: ListItemDataModel)
    }
}