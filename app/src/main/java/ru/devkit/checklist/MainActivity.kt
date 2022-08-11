package ru.devkit.checklist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.Toolbar
import ru.devkit.checklist.ui.presentation.CheckListFragment

class MainActivity : AppCompatActivity() {

    private val presenter by lazy { (application as App).presenter }
    private val router by lazy { (application as App).router }

    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
    }

    override fun onResume() {
        super.onResume()
        router.attach(supportFragmentManager)
        setupCheckList()
    }

    private fun setupCheckList() {
        val fragment = CheckListFragment().apply {
            callback = this@MainActivity.callback
            presenter = this@MainActivity.presenter
            router = this@MainActivity.router
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, CheckListFragment.TAG)
            .commit()
    }

    override fun onPause() {
        super.onPause()
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

    private val callback = object : CheckListFragment.Callback {
        override fun onSelectionMode(checked: Boolean) {
            if (checked) {
                startSupportActionMode(actionModeCallback)
            } else {
                actionModeCallback.mode?.finish()
            }
        }

        override fun onShowSelectionCount(count: Int) {
            actionModeCallback.mode?.title = getString(R.string.action_mode_selected_count, count)
        }

        override fun onShowMessage(text: String) {
            Toast.makeText(this@MainActivity, text, Toast.LENGTH_SHORT).show()
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