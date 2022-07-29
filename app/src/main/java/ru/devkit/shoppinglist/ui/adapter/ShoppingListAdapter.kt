package ru.devkit.shoppinglist.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.devkit.shoppinglist.R
import ru.devkit.shoppinglist.data.model.ShoppingListItemModel
import ru.devkit.shoppinglist.ui.model.ShoppingListItemUiModel

private const val TYPE_DIVIDER = -1
private const val TYPE_ITEM = 0

class ShoppingListAdapter : RecyclerView.Adapter<ShoppingListAdapter.BaseViewHolder>() {

    private val list = mutableListOf<ShoppingListItemUiModel>()
    var action: (ShoppingListItemModel) -> Unit = {}

    fun updateData(update: List<ShoppingListItemUiModel>) {
        val callback = ShoppingListDiffCallback(list, update)
        val result = DiffUtil.calculateDiff(callback)
        result.dispatchUpdatesTo(this)
        list.apply {
            clear()
            addAll(update)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_ITEM -> {
                val view = inflater.inflate(R.layout.list_item, parent, false)
                ItemViewHolder(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.list_item_divider, parent, false)
                DividerViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (holder) {
            is ItemViewHolder -> {
                val elem = list[position] as? ShoppingListItemUiModel.Item ?: return
                val data = elem.data
                holder.checkBox.apply {
                    text = data.title
                    isChecked = data.checked
                    setOnCheckedChangeListener { _, checked ->
                        data.checked = checked
                        action.invoke(data)
                    }
                }
            }
            else -> Unit
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is ShoppingListItemUiModel.Item -> TYPE_ITEM
            is ShoppingListItemUiModel.Divider -> TYPE_DIVIDER
        }
    }

    abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view)

    inner class DividerViewHolder(view: View) : BaseViewHolder(view)

    inner class ItemViewHolder(view: View) : BaseViewHolder(view) {
        val checkBox: CheckBox by lazy { view.findViewById(R.id.check_box) }
    }
}