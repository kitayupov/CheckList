package ru.devkit.checklist.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.devkit.checklist.R
import ru.devkit.checklist.data.model.ProductDataModel
import ru.devkit.checklist.ui.model.ListItemModel

private const val TYPE_DIVIDER = -1
private const val TYPE_ELEMENT = 0

class CheckListAdapter(
    private var checkedAction: (String) -> Unit = {},
    private var selectAction: (String) -> Unit = {},
    private var expandAction: (Boolean) -> Unit = {}
) : RecyclerView.Adapter<CheckListAdapter.BaseViewHolder>() {

    var selectionMode = false

    private val list = mutableListOf<ListItemModel>()

    fun updateData(update: List<ListItemModel>) {
        val callback = CheckListDiffCallback(list, update)
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
        val model = list[position]
        when (holder) {
            is ElementViewHolder -> {
                val element = model as? ListItemModel.Element ?: return
                holder.bind(element.data)
            }
            is DividerViewHolder -> {
                val divider = model as? ListItemModel.Divider ?: return
                holder.bind(divider)
            }
            else -> Unit
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is ListItemModel.Element -> TYPE_ELEMENT
            is ListItemModel.Divider -> TYPE_DIVIDER
        }
    }

    abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view)

    inner class DividerViewHolder(view: View) : BaseViewHolder(view) {

        private val checkBox: CheckBox by lazy { view.findViewById(R.id.check_box) }
        private val counter: TextView by lazy { view.findViewById(R.id.counter) }
        private val clickable: View by lazy { view.findViewById(R.id.clickable) }

        fun bind(data: ListItemModel.Divider) {
            counter.text = data.count.toString()
            checkBox.isChecked = data.expanded
            checkBox.setOnCheckedChangeListener { _, checked ->
                expandAction.invoke(checked)
            }
            clickable.setOnClickListener { checkBox.performClick() }
        }
    }

    inner class ElementViewHolder(view: View) : BaseViewHolder(view) {

        private val checkBox: CheckBox by lazy { view.findViewById(R.id.check_box) }
        private val clickable: View by lazy { view.findViewById(R.id.clickable) }

        fun bind(data: ProductDataModel) {
            checkBox.apply {
                text = data.title
                isChecked = data.completed
            }
            clickable.isSelected = data.selected
            clickable.setOnClickListener {
                if (selectionMode) {
                    selectAction.invoke(data.title)
                } else {
                    checkedAction.invoke(data.title)
                }
            }
            clickable.setOnLongClickListener {
                selectAction.invoke(data.title)
                true
            }
        }
    }
}