package ru.devkit.shoppinglist.ui.model

import ru.devkit.shoppinglist.data.model.ProductDataModel

sealed class ListItemModel {
    data class Element(val data: ProductDataModel) : ListItemModel()
    class Divider(val expanded: Boolean = false) : ListItemModel()
}