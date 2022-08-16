package ru.devkit.checklist.presentation.checklist

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.devkit.checklist.ui.adapter.CheckListAdapter
import ru.devkit.checklist.ui.adapter.RecyclerViewTouchHelperCallback
import ru.devkit.checklist.ui.model.ListItemModel

class CheckListViewWrapper(
    private val recyclerView: RecyclerView,
    private val adapter: CheckListAdapter
) : CheckListContract.MvpView {

    init {
        val decoration = DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(decoration)
        recyclerView.adapter = adapter

        val swipeController = RecyclerViewTouchHelperCallback(adapter)
        val touchHelper = ItemTouchHelper(swipeController)
        touchHelper.attachToRecyclerView(recyclerView)

        adapter.onStartDragListener = object : RecyclerViewTouchHelperCallback.OnDragListener {
            override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                touchHelper.startDrag(viewHolder)
            }
        }
    }

    override fun showItems(list: List<ListItemModel>) {
        adapter.updateData(list)
    }

    override fun setSelectionMode(checked: Boolean) {
        adapter.selectionMode = checked
    }

    override fun scrollToPosition(position: Int) {
        recyclerView.smoothScrollToPosition(position)
    }
}