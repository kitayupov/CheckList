package ru.devkit.shoppinglist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import ru.devkit.shoppinglist.data.model.ListItemDataModel
import ru.devkit.shoppinglist.ui.adapter.ShoppingListAdapter
import ru.devkit.shoppinglist.ui.additem.CreateNewItemViewRouter
import ru.devkit.shoppinglist.ui.confirmation.ConfirmationDialogFragment
import ru.devkit.shoppinglist.ui.model.ListItemUiModel
import ru.devkit.shoppinglist.ui.presentation.ShoppingListContract

class MainActivity : AppCompatActivity() {

    private val createNewItemViewRouter = CreateNewItemViewRouter(supportFragmentManager)
    private val presenter by lazy { (application as App).presenter }
    private val adapter = ShoppingListAdapter()

    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }

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
    }

    override fun onPause() {
        super.onPause()
        presenter.detachView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_actions, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_actions_clear_data -> {
                showConfirmationDialog(
                    title = getString(R.string.dialog_clear_all_title),
                    confirmButton = getString(R.string.dialog_clear_button),
                    action = presenter::clearData
                )
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun showConfirmationDialog(
        title: String,
        message: String? = null,
        confirmButton: String? = null,
        action: () -> Unit
    ) {
        ConfirmationDialogFragment.newInstance(title, message, confirmButton).apply {
            confirmAction = action
            show(supportFragmentManager, ConfirmationDialogFragment.TAG)
        }
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(decoration)
        recyclerView.adapter = adapter
        adapter.checkedAction = presenter::updateItem
        adapter.selectAction = presenter::selectItem
        adapter.expandAction = presenter::expandArchived
    }

    private fun setupActionButton() {
        val floatingActionButton = findViewById<View>(R.id.floating_button)
        floatingActionButton.setOnClickListener {
            createNewItemViewRouter.showCreateNewItemView() {
                presenter.addItem(ListItemDataModel(it))
            }
        }
    }

    inner class MvpViewImpl : ShoppingListContract.MvpView {
        override fun showItems(list: List<ListItemUiModel>) {
            adapter.updateData(list)
        }

        override fun selectionMode(checked: Boolean) {
            adapter.selectionMode = checked
            if (checked) {
                startSupportActionMode(actionModeCallback)
            } else {
                actionModeCallback.mode?.finish()
            }
        }
    }

    private val actionModeCallback = object : ActionMode.Callback {

        var mode: ActionMode? = null

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.menu_contextual_delete -> {
                    showConfirmationDialog(
                        title = getString(R.string.dialog_remove_selected_title),
                        confirmButton = getString(R.string.dialog_remove_button),
                        action = presenter::removeSelected
                    )
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