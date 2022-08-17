package ru.devkit.checklist.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.devkit.checklist.data.model.ProductDataModel

class CheckListDiffCallback(
    private val oldList: List<ProductDataModel>,
    private val newList: List<ProductDataModel>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].title == newList[newItemPosition].title
    }
}