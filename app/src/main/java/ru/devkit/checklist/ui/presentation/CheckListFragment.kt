package ru.devkit.checklist.ui.presentation

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(view)
        setupActionButton(view)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        presenter?.attachView(this)
        floatingActionButton?.show()
    }

    override fun onDetach() {
        super.onDetach()
        presenter?.detachView()
        floatingActionButton?.hide()
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
        adapter.selectionMode = checked
        if (checked) {
            (activity as? AppCompatActivity)?.startSupportActionMode(actionModeCallback)
            floatingActionButton?.hide()
        } else {
            actionModeCallback.mode?.finish()
            floatingActionButton?.show()
        }
    }

    override fun showSelectedCount(value: Int) {
        actionModeCallback.mode?.title = getString(R.string.action_mode_selected_count, value)
    }

    override fun showMessage(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_actions, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_actions_clear_data -> {
                router?.showClearAllConfirmation { presenter?.clearData() }
                true
            }
            R.id.menu_actions_sort_ranking -> {
                presenter?.setSortRanking()
                true
            }
            R.id.menu_actions_sort_default -> {
                presenter?.setSortDefault()
                true
            }
            R.id.menu_actions_sort_name -> {
                presenter?.setSortName()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private val actionModeCallback = object : ActionMode.Callback {

        var mode: ActionMode? = null

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.menu_contextual_delete -> {
                    router?.showRemoveSelectedConfirmation { presenter?.removeSelected() }
                    true
                }
                R.id.menu_contextual_select_all -> {
                    presenter?.selectAll()
                    true
                }
                R.id.menu_contextual_check_selected -> {
                    presenter?.checkSelected()
                    true
                }
                R.id.menu_contextual_uncheck_selected -> {
                    presenter?.uncheckSelected()
                    true
                }
                else -> return false
            }
        }

        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            activity?.menuInflater?.inflate(R.menu.menu_contextual, menu)
            this.mode = mode
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            // no-op
            return false
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            presenter?.clearSelected()
        }
    }
}