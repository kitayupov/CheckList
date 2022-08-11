package ru.devkit.checklist.ui.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.devkit.checklist.R
import ru.devkit.checklist.router.CheckListRouter
import ru.devkit.checklist.ui.adapter.CheckListAdapter
import ru.devkit.checklist.ui.model.ListItemModel

class CheckListFragment : Fragment(R.layout.fragment_check_list), CheckListContract.MvpView {

    companion object {
        const val TAG = "CheckListFragment"
    }

    private var floatingActionButton: FloatingActionButton? = null

    private val adapter = CheckListAdapter()

    var presenter: CheckListPresenter? = null
    var router: CheckListRouter? = null

    var callback: Callback? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(view)
        setupActionButton(view)
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

    private fun setupActionButton(view: View) {
        floatingActionButton = view.findViewById<FloatingActionButton?>(R.id.floating_button).apply {
            setOnClickListener {
                router?.showCreateItemView(
                    onCreate = { name -> presenter?.createItem(name) },
                    onDismiss = { show() }
                )
                hide()
            }
        }
    }

    override fun showItems(list: List<ListItemModel>) {
        adapter.updateData(list)
    }

    override fun setSelectionMode(checked: Boolean) {
        callback?.onSelectionMode(checked)
        adapter.selectionMode = checked
        if (checked) {
            floatingActionButton?.hide()
        } else {
            floatingActionButton?.show()
        }
    }

    override fun showSelectedCount(value: Int) {
        callback?.onShowSelectionCount(value)
    }

    override fun showMessage(text: String) {
        callback?.onShowMessage(text)
    }

    interface Callback {
        fun onSelectionMode(checked: Boolean)
        fun onShowSelectionCount(count: Int)
        fun onShowMessage(text: String)
    }
}