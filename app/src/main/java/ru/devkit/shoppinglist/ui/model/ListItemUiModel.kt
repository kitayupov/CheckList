package ru.devkit.shoppinglist.ui.model

import ru.devkit.shoppinglist.data.model.ProductDataModel

sealed class ListItemUiModel {
    data class Element(val data: ProductDataModel) : ListItemUiModel()
    class Divider(val expanded: Boolean = false) : ListItemUiModel()
}