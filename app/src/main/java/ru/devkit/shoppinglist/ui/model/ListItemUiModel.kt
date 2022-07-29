package ru.devkit.shoppinglist.ui.model

import ru.devkit.shoppinglist.data.model.ListItemDataModel

sealed class ListItemUiModel(open val data: ListItemDataModel) {
    data class Element(override val data: ListItemDataModel) : ListItemUiModel(data)
    object Divider : ListItemUiModel(ListItemDataModel.EMPTY)
}