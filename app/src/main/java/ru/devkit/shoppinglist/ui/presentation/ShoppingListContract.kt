package ru.devkit.shoppinglist.ui.presentation

import ru.devkit.shoppinglist.data.model.ShoppingListItemModel
import ru.devkit.shoppinglist.ui.model.ShoppingListItemUiModel

object ShoppingListContract {

    interface MvpView {
        fun showItems(list: List<ShoppingListItemUiModel>)
    }

    interface MvpPresenter {
        fun attachView(view: MvpView)
        fun detachView()
        fun addItem(item: ShoppingListItemModel)
        fun updateItem(item: ShoppingListItemModel)
        fun removeItem(item: ShoppingListItemModel)
    }
}