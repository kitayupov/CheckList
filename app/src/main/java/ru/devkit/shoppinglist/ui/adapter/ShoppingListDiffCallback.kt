package ru.devkit.shoppinglist.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.devkit.shoppinglist.data.model.ShoppingListItemModel

class ShoppingListDiffCallback(
    private val oldList: List<ShoppingListItemModel>,
    private val newList: List<ShoppingListItemModel>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}