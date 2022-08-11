package ru.devkit.checklist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.devkit.checklist.ui.presentation.CheckListFragment

class MainActivity : AppCompatActivity() {

    private val presenter by lazy { (application as App).presenter }
    private val router by lazy { (application as App).router }

    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private val floatingActionButton by lazy { findViewById<FloatingActionButton>(R.id.floating_button) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setupActionButton()
    }

    override fun onResume() {
        super.onResume()
        router.attach(supportFragmentManager)
        setupCheckList()
    }

    private fun setupCheckList() {
        val fragment = CheckListFragment().apply {
            callback = checkListCallback
            presenter.attachView(this)
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, CheckListFragment.TAG)
            .commit()
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
            R.id.menu_actions_sort_ranking -> {
                presenter.setSortRanking()
                true
            }
            R.id.menu_actions_sort_default -> {
                presenter.setSortDefault()
                true
            }
            R.id.menu_actions_sort_name -> {
                presenter.setSortName()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun setupActionButton() {
        floatingActionButton.setOnClickListener {
            router.showCreateItemView(
                onCreate = presenter::createItem,
                onDismiss = floatingActionButton::show
            )
            floatingActionButton.hide()
        }
    }

    private val checkListCallback = object : CheckListFragment.Callback {
        override fun onSwitchChecked(name: String) {
            presenter.switchChecked(name)
        }

        override fun onSwitchSelected(name: String) {
            presenter.switchSelected(name)
        }

        override fun onExpandCompleted(checked: Boolean) {
            presenter.expandCompleted(checked)
        }

        override fun onSelectionMode(checked: Boolean) {
            if (checked) {
                startSupportActionMode(actionModeCallback)
                floatingActionButton.hide()
            } else {
                actionModeCallback.mode?.finish()
                floatingActionButton.show()
            }
        }

        override fun onShowSelectionCount(count: Int) {
            actionModeCallback.mode?.title = getString(R.string.action_mode_selected_count, count)
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