package ru.devkit.checklist.ui.model

import ru.devkit.checklist.data.model.ProductDataModel

sealed class ListItemModel {
    data class Element(val data: ProductDataModel) : ListItemModel()
    class Divider(val count: Int = 0, val expanded: Boolean = false) : ListItemModel()
}