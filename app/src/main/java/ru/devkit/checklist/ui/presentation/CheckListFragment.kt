package ru.devkit.checklist.ui.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import ru.devkit.checklist.R
import ru.devkit.checklist.ui.adapter.CheckListAdapter
import ru.devkit.checklist.ui.model.ListItemModel

class CheckListFragment : Fragment(R.layout.fragment_check_list), CheckListContract.MvpView {

    companion object {
        const val TAG = "CheckListFragment"
    }

    private val adapter = CheckListAdapter()
    var presenter: CheckListPresenter? = null

    var callback: Callback? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(view)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        presenter?.attachView(this)
    }

    override fun onDetach() {
        super.onDetach()
        presenter?.detachView()
        callback = null
    }

    private fun setupRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        val decoration = DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(decoration)
        recyclerView.adapter = adapter.apply {
            checkedAction = { name -> presenter?.switchChecked(name) }
            selectAction = { name -> presenter?.switchSelected(name) }
            expandAction = { checked -> presenter?.expandCompleted(checked) }
        }
    }

    override fun showItems(list: List<ListItemModel>) {
        adapter.updateData(list)
    }

    override fun setSelectionMode(checked: Boolean) {
        adapter.selectionMode = checked
        callback?.onSelectionMode(checked)
    }

    override fun showSelectedCount(value: Int) {
        callback?.onShowSelectionCount(value)
    }

    override fun showMessage(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    interface Callback {
        fun onSelectionMode(checked: Boolean)
        fun onShowSelectionCount(count: Int)
    }
}