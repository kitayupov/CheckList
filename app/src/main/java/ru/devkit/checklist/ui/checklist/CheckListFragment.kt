package ru.devkit.checklist.ui.checklist

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.devkit.checklist.R
import ru.devkit.checklist.presentation.actionmode.ActionModePresenter
import ru.devkit.checklist.presentation.actionmode.ActionModeViewWrapper
import ru.devkit.checklist.presentation.checklist.CheckListPresenter
import ru.devkit.checklist.presentation.checklist.CheckListViewWrapper
import ru.devkit.checklist.presentation.createitemaction.CreateItemActionPresenter
import ru.devkit.checklist.presentation.createitemaction.CreateItemActionViewWrapper
import ru.devkit.checklist.router.CheckListRouter
import ru.devkit.checklist.ui.adapter.CheckListAdapter

class CheckListFragment : Fragment(R.layout.fragment_check_list) {

    companion object {
        const val TAG = "CheckListFragment"
    }

    private var wrapper: ActionModeViewWrapper? = null

    var checkListPresenter: CheckListPresenter? = null
    var actionModePresenter: ActionModePresenter? = null
    var createItemActionPresenter: CreateItemActionPresenter? = null

    var router: CheckListRouter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setupRecyclerView(view)
        setupFloatingActionButton(view)
        setupActionToolbar()
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
        actionModePresenter?.detachView()
        createItemActionPresenter?.detachView()
    }

    private fun setupRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        val adapter = CheckListAdapter(
            checkedAction = { name -> checkListPresenter?.switchChecked(name) },
            selectAction = { name -> checkListPresenter?.switchSelected(name) },
            expandAction = { checked -> checkListPresenter?.expandCompleted(checked) }
        )
        checkListPresenter?.attachView(CheckListViewWrapper(recyclerView, adapter))
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

    private fun setupActionToolbar() {
        wrapper = ActionModeViewWrapper(activity, checkListPresenter, router)
            .also { actionModePresenter?.attachView(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        wrapper?.setOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return wrapper?.onOptionsItemSelected(item) ?: super.onOptionsItemSelected(item)
    }
}