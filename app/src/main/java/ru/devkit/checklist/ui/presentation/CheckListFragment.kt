package ru.devkit.checklist.ui.presentation

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.devkit.checklist.R
import ru.devkit.checklist.presentation.createitemaction.CreateItemActionPresenter
import ru.devkit.checklist.presentation.createitemaction.CreateItemActionViewWrapper
import ru.devkit.checklist.presentation.toolbar.ActionModePresenter
import ru.devkit.checklist.presentation.toolbar.ActionModeViewWrapper
import ru.devkit.checklist.router.CheckListRouter
import ru.devkit.checklist.ui.adapter.CheckListAdapter
import ru.devkit.checklist.ui.model.ListItemModel

class CheckListFragment : Fragment(R.layout.fragment_check_list), CheckListContract.MvpView {

    companion object {
        const val TAG = "CheckListFragment"
    }

    private val adapter = CheckListAdapter()

    var createItemActionPresenter: CreateItemActionPresenter? = null
    var checkListPresenter: CheckListPresenter? = null
    var actionModePresenter: ActionModePresenter? = null

    var router: CheckListRouter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(view)
        setupFloatingActionButton(view)
        setupActionMode()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        checkListPresenter?.attachView(this)
    }

    override fun onResume() {
        super.onResume()
        createItemActionPresenter?.showView()
    }

    override fun onPause() {
        super.onPause()
        createItemActionPresenter?.hideView()
    }

    override fun onDetach() {
        super.onDetach()
        checkListPresenter?.detachView()
        createItemActionPresenter?.detachView()
        actionModePresenter?.detachView()
    }

    private fun setupRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        val decoration = DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(decoration)
        recyclerView.adapter = adapter.apply {
            checkedAction = { name -> checkListPresenter?.switchChecked(name) }
            selectAction = { name -> checkListPresenter?.switchSelected(name) }
            expandAction = { checked -> checkListPresenter?.expandCompleted(checked) }
        }
    }

    private fun setupFloatingActionButton(view: View) {
        val fab = view.findViewById<FloatingActionButton?>(R.id.floating_button).apply {
            setOnClickListener {
                router?.showCreateItemView(
                    onCreate = { name -> checkListPresenter?.createItem(name) },
                    onDismiss = { show() }
                )
                hide()
            }
        }
        createItemActionPresenter?.attachView(CreateItemActionViewWrapper(fab))
    }

    private fun setupActionMode() {
        val wrapper = ActionModeViewWrapper(activity, checkListPresenter, router)
        actionModePresenter?.attachView(wrapper)
    }

    override fun showItems(list: List<ListItemModel>) {
        adapter.updateData(list)
    }

    override fun setSelectionMode(checked: Boolean) {
        adapter.selectionMode = checked
        if (checked) {
            createItemActionPresenter?.hideView()
        } else {
            createItemActionPresenter?.showView()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.menu_actions, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_actions_clear_data -> {
                router?.showClearAllConfirmation { checkListPresenter?.clearData() }
                true
            }
            R.id.menu_actions_sort_ranking -> {
                checkListPresenter?.setSortRanking()
                true
            }
            R.id.menu_actions_sort_default -> {
                checkListPresenter?.setSortDefault()
                true
            }
            R.id.menu_actions_sort_name -> {
                checkListPresenter?.setSortName()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}