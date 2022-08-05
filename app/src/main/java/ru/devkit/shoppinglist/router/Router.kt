package ru.devkit.shoppinglist.router

import android.content.Context
import androidx.fragment.app.FragmentManager
import ru.devkit.shoppinglist.R
import ru.devkit.shoppinglist.ui.confirmation.ConfirmationDialogFragment
import ru.devkit.shoppinglist.ui.createitem.CreateItemBottomSheetFragment

class Router(private val context: Context) {

    private var supportFragmentManager: FragmentManager? = null

    fun attach(fragmentManager: FragmentManager) {
        this.supportFragmentManager = fragmentManager
    }

    fun detach() {
        this.supportFragmentManager = null
    }

    fun showCreateItemView(action: (String) -> Unit) {
        supportFragmentManager?.let {
            CreateItemBottomSheetFragment().apply {
                createAction = action
                show(it, CreateItemBottomSheetFragment.TAG)
            }
        }
    }

    fun showClearAllConfirmation(action: () -> Unit) {
        showConfirmation(
            title = context.getString(R.string.dialog_clear_all_title),
            confirmButton = context.getString(R.string.dialog_clear_button),
            action = action
        )
    }

    fun showRemoveSelectedConfirmation(action: () -> Unit) {
        showConfirmation(
            title = context.getString(R.string.dialog_remove_selected_title),
            confirmButton = context.getString(R.string.dialog_remove_button),
            action = action
        )
    }

    private fun showConfirmation(
        title: String,
        message: String? = null,
        confirmButton: String,
        action: () -> Unit
    ) {
        supportFragmentManager?.let {
            ConfirmationDialogFragment.newInstance(title, message, confirmButton).apply {
                confirmAction = action
                show(it, ConfirmationDialogFragment.TAG)
            }
        }
    }
}