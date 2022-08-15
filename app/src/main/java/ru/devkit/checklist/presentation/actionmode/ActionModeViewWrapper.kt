package ru.devkit.checklist.presentation.actionmode

import android.app.Activity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import ru.devkit.checklist.R
import ru.devkit.checklist.router.CheckListRouter
import ru.devkit.checklist.ui.presentation.CheckListPresenter

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