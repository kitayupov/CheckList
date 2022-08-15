package ru.devkit.checklist.presentation.actionmode

import android.app.Activity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import ru.devkit.checklist.R
import ru.devkit.checklist.presentation.checklist.CheckListPresenter
import ru.devkit.checklist.router.CheckListRouter

class ActionModeViewWrapper(
    private val activity: Activity?,
    private val checkListPresenter: CheckListPresenter?,
    private val router: CheckListRouter?
) : ActionModeContract.MvpView {

    override fun setSelectionMode(checked: Boolean) {
        if (checked) {
            (activity as? AppCompatActivity)?.startSupportActionMode(actionModeCallback)
        } else {
            actionModeCallback.mode?.finish()
        }
    }

    override fun setTitle(title: String) {
        actionModeCallback.mode?.title = title
    }

    fun setOptionsMenu(menu: Menu) {
        activity?.menuInflater?.inflate(R.menu.menu_actions, menu)
    }

    fun onOptionsItemSelected(item: MenuItem): Boolean? {
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
            else -> null
        }
    }

    private val actionModeCallback = object : ActionMode.Callback {

        var mode: ActionMode? = null

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.menu_contextual_delete -> {
                    router?.showRemoveSelectedConfirmation { checkListPresenter?.removeSelected() }
                    true
                }
                R.id.menu_contextual_select_all -> {
                    checkListPresenter?.selectAll()
                    true
                }
                R.id.menu_contextual_check_selected -> {
                    checkListPresenter?.checkSelected()
                    true
                }
                R.id.menu_contextual_uncheck_selected -> {
                    checkListPresenter?.uncheckSelected()
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
            checkListPresenter?.clearSelected()
        }
    }
}