package ru.devkit.checklist.presentation.checklist

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.devkit.checklist.ui.adapter.CheckListAdapter
import ru.devkit.checklist.ui.adapter.RecyclerViewTouchHelperCallback
import ru.devkit.checklist.ui.model.ListItemModel

class CheckListViewWrapper(
    private val recyclerViewActual: RecyclerView,
    private val adapterActual: CheckListAdapter,
    private val recyclerViewCompleted: RecyclerView,
    private val adapterCompleted: CheckListAdapter
) : CheckListContract.MvpView {

    init {
        val decoration = DividerItemDecoration(recyclerViewActual.context, DividerItemDecoration.VERTICAL)
        recyclerViewActual.addItemDecoration(decoration)
        recyclerViewActual.adapter = adapterActual

        recyclerViewCompleted.addItemDecoration(decoration)
        recyclerViewCompleted.adapter = adapterCompleted

        val swipeController = RecyclerViewTouchHelperCallback(adapterActual)
        val touchHelper = ItemTouchHelper(swipeController)
        touchHelper.attachToRecyclerView(recyclerViewActual)

        adapterActual.onStartDragListener = object : RecyclerViewTouchHelperCallback.OnDragListener {
            override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                touchHelper.startDrag(viewHolder)
            }
        }
    }

    override fun showItems(list: List<ListItemModel>) {
        adapterActual.updateData(list)
    }

//    override fun showItems(unchecked: List<ListItemModel>, checked: List<ListItemModel>) {
//        adapterActual.updateData(unchecked)
//        adapterCompleted.updateData(checked)
//    }

    override fun setSelectionMode(checked: Boolean) {
        adapterActual.selectionMode = checked
    }

    override fun scrollToPosition(position: Int) {
        recyclerViewActual.smoothScrollToPosition(position)
    }
}