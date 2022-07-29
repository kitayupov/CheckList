package ru.devkit.shoppinglist.ui.model

import ru.devkit.shoppinglist.data.model.ShoppingListItemModel

sealed class ShoppingListItemUiModel {
    object Divider : ShoppingListItemUiModel()
    data class Item(val data: ShoppingListItemModel) : ShoppingListItemUiModel()
}