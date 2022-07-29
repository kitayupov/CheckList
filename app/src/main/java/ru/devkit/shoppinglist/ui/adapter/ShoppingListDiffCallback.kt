package ru.devkit.shoppinglist.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.devkit.shoppinglist.ui.model.ListItemUiModel

class ShoppingListDiffCallback(
    private val oldList: List<ListItemUiModel>,
    private val newList: List<ListItemUiModel>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].data == newList[newItemPosition].data
    }
}