package ru.devkit.checklist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.devkit.checklist.data.model.ProductDataModel
import ru.devkit.checklist.ui.adapter.CheckListAdapter
import ru.devkit.checklist.ui.model.ListItemModel
import ru.devkit.checklist.ui.presentation.CheckListContract

class MainActivity : AppCompatActivity() {

    private val presenter by lazy { (application as App).presenter }
    private val router by lazy { (application as App).router }

    private val adapter = CheckListAdapter()

    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private val floatingActionButton by lazy { findViewById<FloatingActionButton>(R.id.floating_button) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setupRecyclerView()
        setupActionButton()
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(MvpViewImpl())
        router.attach(supportFragmentManager)
    }

    override fun onPause() {
        super.onPause()
        presenter.detachView()
        router.detach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_actions, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_actions_clear_data -> {
                router.showClearAllConfirmation(presenter::clearData)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(decoration)
        recyclerView.adapter = adapter
        adapter.checkedAction = presenter::updateItem
        adapter.selectAction = presenter::selectItem
        adapter.expandAction = presenter::expandCompleted
    }

    private fun setupActionButton() {
        floatingActionButton.setOnClickListener {
            router.showCreateItemView(
                onCreate = { presenter.createItem(ProductDataModel(it)) },
                onDismiss = { floatingActionButton.show() }
            )
            floatingActionButton.hide()
        }
    }

    inner class MvpViewImpl : CheckListContract.MvpView {
        override fun showItems(list: List<ListItemModel>) {
            adapter.updateData(list)
        }

        override fun setSelectionMode(checked: Boolean) {
            adapter.selectionMode = checked
            if (checked) {
                startSupportActionMode(actionModeCallback)
                floatingActionButton.hide()
            } else {
                actionModeCallback.mode?.finish()
                floatingActionButton.show()
            }
        }

        override fun showSelectedCount(value: Int) {
            actionModeCallback.mode?.title = getString(R.string.action_mode_selected_count, value)
        }
    }

    private val actionModeCallback = object : ActionMode.Callback {

        var mode: ActionMode? = null

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.menu_contextual_delete -> {
                    router.showRemoveSelectedConfirmation(presenter::removeSelected)
                    true
                }
                R.id.menu_contextual_select_all -> {
                    presenter.selectAll()
                    true
                }
                R.id.menu_contextual_check_selected -> {
                    presenter.checkSelected()
                    true
                }
                R.id.menu_contextual_uncheck_selected -> {
                    presenter.uncheckSelected()
                    true
                }
                else -> return false
            }
        }

        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            menuInflater.inflate(R.menu.menu_contextual, menu)
            this.mode = mode
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            // no-op
            return false
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            presenter.clearSelected()
        }
    }
}