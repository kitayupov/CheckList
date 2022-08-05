package ru.devkit.shoppinglist.ui.model

import ru.devkit.shoppinglist.data.model.ListItemDataModel

sealed class ListItemUiModel {
    data class Element(val data: ListItemDataModel) : ListItemUiModel()
    class Divider(val expanded: Boolean = false) : ListItemUiModel()
}