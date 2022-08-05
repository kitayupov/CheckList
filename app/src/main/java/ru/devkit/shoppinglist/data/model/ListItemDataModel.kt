package ru.devkit.shoppinglist.data.model

data class ListItemDataModel(
    val title: String,
    var checked: Boolean = false,
    var selected: Boolean = false
) {
    companion object {
        val EMPTY = ListItemDataModel(title = "")
    }
}