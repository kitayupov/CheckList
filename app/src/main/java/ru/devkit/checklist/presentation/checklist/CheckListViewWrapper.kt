package ru.devkit.checklist.presentation.checklist

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.devkit.checklist.data.model.ProductDataModel
import ru.devkit.checklist.ui.adapter.CheckListAdapter
import ru.devkit.checklist.ui.adapter.RecyclerViewTouchHelperCallback

class CheckListViewWrapper(
    private val recyclerViewActual: RecyclerView,
    private val adapterActual: CheckListAdapter,
    private val recyclerViewCompleted: RecyclerView,
    private val adapterCompleted: CheckListAdapter
) : CheckListContract.MvpView {

    init {
        // todo avoid duplications
        val decoration = DividerItemDecoration(recyclerViewActual.context, DividerItemDecoration.VERTICAL)
        recyclerViewActual.addItemDecoration(decoration)
        recyclerViewActual.adapter = adapterActual

        recyclerViewCompleted.addItemDecoration(decoration)
        recyclerViewCompleted.adapter = adapterCompleted

        val swipeController = RecyclerViewTouchHelperCallback(adapterActual)
        val touchHelper = ItemTouchHelper(swipeController)
        touchHelper.attachToRecyclerView(recyclerViewActual)

        // todo do we need dragging for completed?
        adapterActual.onStartDragListener = object : RecyclerViewTouchHelperCallback.OnDragListener {
            override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                touchHelper.startDrag(viewHolder)
            }
        }
    }

    override fun showItems(actual: List<ProductDataModel>, completed: List<ProductDataModel>) {
        adapterActual.updateData(actual)
        adapterCompleted.updateData(completed)
    }

    override fun setSelectionMode(checked: Boolean) {
        adapterActual.selectionMode = checked
        adapterCompleted.selectionMode = checked
    }

    override fun scrollToPosition(position: Int) {
        recyclerViewActual.smoothScrollToPosition(position)
    }
}