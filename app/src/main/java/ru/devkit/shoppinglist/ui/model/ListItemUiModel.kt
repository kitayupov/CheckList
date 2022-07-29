package ru.devkit.shoppinglist.ui.model

import ru.devkit.shoppinglist.data.model.ListItemDataModel

sealed class ListItemUiModel {
    data class Element(val data: ListItemDataModel) : ListItemUiModel()
    object Divider : ListItemUiModel()
}