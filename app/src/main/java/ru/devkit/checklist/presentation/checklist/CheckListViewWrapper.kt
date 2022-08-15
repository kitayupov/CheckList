package ru.devkit.checklist.presentation.checklist

import ru.devkit.checklist.ui.adapter.CheckListAdapter
import ru.devkit.checklist.ui.model.ListItemModel
import ru.devkit.checklist.ui.presentation.CheckListContract

class CheckListViewWrapper(
    private val adapter: CheckListAdapter
) : CheckListContract.MvpView {

    override fun showItems(list: List<ListItemModel>) {
        adapter.updateData(list)
    }

    override fun setSelectionMode(checked: Boolean) {
        adapter.selectionMode = checked
    }
}