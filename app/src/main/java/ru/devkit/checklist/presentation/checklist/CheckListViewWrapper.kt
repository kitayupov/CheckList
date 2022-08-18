package ru.devkit.checklist.presentation.checklist

import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.devkit.checklist.data.model.ProductDataModel
import ru.devkit.checklist.ui.DividerView
import ru.devkit.checklist.ui.adapter.CheckListAdapter
import ru.devkit.checklist.ui.adapter.RecyclerViewTouchHelperCallback

class CheckListViewWrapper(
    private val recyclerViewActual: RecyclerView,
    private val adapterActual: CheckListAdapter,
    private val recyclerViewCompleted: RecyclerView,
    private val adapterCompleted: CheckListAdapter,
    private val divider: DividerView
) : CheckListContract.MvpView {

    init {
        setupRecyclerView(recyclerViewActual, adapterActual)
        setupRecyclerView(recyclerViewCompleted, adapterCompleted)
    }

    override fun showItems(actual: List<ProductDataModel>, completed: List<ProductDataModel>) {
        adapterActual.updateData(actual)
        adapterCompleted.updateData(completed)
        divider.count = completed.size
    }

    override fun setSelectionMode(checked: Boolean) {
        adapterActual.selectionMode = checked
        adapterCompleted.selectionMode = checked
    }

    override fun scrollToPosition(position: Int) {
        recyclerViewActual.smoothScrollToPosition(position)
    }

    override fun expandCompleted(checked: Boolean) {
        recyclerViewCompleted.isVisible = checked
        divider.expanded = checked
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, adapter: CheckListAdapter) {
        val decoration = DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(decoration)
        recyclerView.adapter = adapter
        // touch helper
        val swipeController = RecyclerViewTouchHelperCallback(adapter)
        val touchHelper = ItemTouchHelper(swipeController)
        touchHelper.attachToRecyclerView(recyclerView)
        adapter.onStartDragListener = object : RecyclerViewTouchHelperCallback.OnDragListener {
            override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                touchHelper.startDrag(viewHolder)
            }
        }
    }
}