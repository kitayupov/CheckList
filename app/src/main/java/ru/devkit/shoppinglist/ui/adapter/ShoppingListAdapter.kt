package ru.devkit.shoppinglist.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.devkit.shoppinglist.R
import ru.devkit.shoppinglist.data.model.ShoppingListItemModel

class ShoppingListAdapter : RecyclerView.Adapter<ShoppingListAdapter.ItemViewHolder>() {

    private val list = mutableListOf<ShoppingListItemModel>()
    var action: (ShoppingListItemModel) -> Unit = {}

    fun updateData(update: List<ShoppingListItemModel>) {
        val callback = ShoppingListDiffCallback(list, update)
        val result = DiffUtil.calculateDiff(callback)
        result.dispatchUpdatesTo(this)
        list.apply {
            clear()
            addAll(update)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val elem = list[position]
        holder.checkBox.apply {
            text = elem.title
            isChecked = elem.checked
            setOnCheckedChangeListener { _, checked ->
                elem.checked = checked
                action.invoke(elem)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox by lazy { view.findViewById(R.id.check_box) }
    }
}