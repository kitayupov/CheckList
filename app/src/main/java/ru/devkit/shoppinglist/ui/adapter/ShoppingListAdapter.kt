package ru.devkit.shoppinglist.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.devkit.shoppinglist.R
import ru.devkit.shoppinglist.data.model.ListItemDataModel
import ru.devkit.shoppinglist.ui.model.ListItemUiModel

private const val TYPE_DIVIDER = -1
private const val TYPE_ELEMENT = 0

class ShoppingListAdapter : RecyclerView.Adapter<ShoppingListAdapter.BaseViewHolder>() {

    var checkedAction: (ListItemDataModel) -> Unit = {}
    var expandAction: (Boolean) -> Unit = {}
    var selectAction: (ListItemDataModel) -> Unit = {}

    var selectionMode = false

    private val list = mutableListOf<ListItemUiModel>()

    fun updateData(update: List<ListItemUiModel>) {
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
            TYPE_ELEMENT -> {
                val view = inflater.inflate(R.layout.list_item_element, parent, false)
                ElementViewHolder(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.list_item_divider, parent, false)
                DividerViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (holder) {
            is ElementViewHolder -> holder.bind(list[position].data)
            is DividerViewHolder -> holder.bind(list[position].data)
            else -> Unit
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is ListItemUiModel.Element -> TYPE_ELEMENT
            is ListItemUiModel.Divider -> TYPE_DIVIDER
        }
    }

    abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view)

    inner class DividerViewHolder(view: View) : BaseViewHolder(view) {

        private val chevron: CheckBox by lazy { view.findViewById(R.id.chevron) }
        private val clickable: View by lazy { view.findViewById(R.id.clickable) }

        fun bind(data: ListItemDataModel) {
            chevron.isChecked = data.checked
            clickable.setOnClickListener {
                chevron.isChecked = chevron.isChecked.not()
                expandAction.invoke(chevron.isChecked)
            }
        }
    }

    inner class ElementViewHolder(view: View) : BaseViewHolder(view) {

        private val checkBox: CheckBox by lazy { view.findViewById(R.id.check_box) }
        private val clickable: View by lazy { view.findViewById(R.id.clickable) }

        fun bind(data: ListItemDataModel) {
            checkBox.apply {
                text = data.title
                isChecked = data.checked
            }
            clickable.isSelected = data.selected
            clickable.setOnClickListener {
                if (selectionMode) {
                    selectAction.invoke(data.copy(selected = data.selected.not()))
                } else {
                    checkedAction.invoke(data.copy(checked = data.checked.not()))
                }
            }
            clickable.setOnLongClickListener {
                selectAction.invoke(data.copy(selected = data.selected.not()))
                true
            }
        }
    }
}