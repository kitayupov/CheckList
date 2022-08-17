package ru.devkit.checklist.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.devkit.checklist.R
import ru.devkit.checklist.data.model.ProductDataModel
import java.util.*

class CheckListAdapter(
    private var checkedAction: (String) -> Unit = {},
    private var selectAction: (String) -> Unit = {},
    private var expandAction: (Boolean) -> Unit = {},
    private var reorderAction: (List<ProductDataModel>) -> Unit = {}
) : RecyclerView.Adapter<CheckListAdapter.ElementViewHolder>(), RecyclerViewTouchHelperCallback.ItemTouchHelperAdapter {

    var selectionMode = false

    private val list = mutableListOf<ProductDataModel>()

    var onStartDragListener: RecyclerViewTouchHelperCallback.OnDragListener? = null

    fun updateData(update: List<ProductDataModel>) {
        val callback = CheckListDiffCallback(list, update)
        val result = DiffUtil.calculateDiff(callback)
        result.dispatchUpdatesTo(this)
        list.apply {
            clear()
            addAll(update)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElementViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item_element, parent, false)
        return ElementViewHolder(view)
    }

    override fun onBindViewHolder(holder: ElementViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

//    inner class DividerViewHolder(view: View) : BaseViewHolder(view) {
//
//        private val checkBox: CheckBox by lazy { view.findViewById(R.id.check_box) }
//        private val counter: TextView by lazy { view.findViewById(R.id.counter) }
//        private val clickable: View by lazy { view.findViewById(R.id.clickable) }
//
//        fun bind(data: ListItemModel.Divider) {
//            counter.text = data.count.toString()
//            checkBox.isChecked = data.expanded
//            checkBox.setOnCheckedChangeListener { _, checked ->
//                expandAction.invoke(checked)
//            }
//            clickable.setOnClickListener { checkBox.performClick() }
//        }
//    }

    inner class ElementViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val checkBox: CheckBox by lazy { view.findViewById(R.id.check_box) }
        private val clickable: View by lazy { view.findViewById(R.id.clickable) }
        private val handle: View by lazy { view.findViewById(R.id.handle) }

        @SuppressLint("ClickableViewAccessibility")
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
            handle.setOnTouchListener { _, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                    onStartDragListener?.onStartDrag(this)
                }
                return@setOnTouchListener false
            }
        }
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(list, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(list, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
        reorderAction.invoke(list)
    }

    override fun onItemSwiped(position: Int) {
        checkedAction.invoke(list[position].title)
        notifyItemChanged(position)
    }
}