package ru.devkit.shoppinglist.ui.presentation

import ru.devkit.shoppinglist.data.model.ShoppingListItemModel

object ShoppingListContract {

    interface MvpView {
        fun showItems(list: List<ShoppingListItemModel>)
    }

    interface MvpPresenter {
        fun attachView(view: MvpView)
        fun detachView()
        fun addItem(item: ShoppingListItemModel)
        fun removeItem(item: ShoppingListItemModel)
    }
}