package ru.devkit.shoppinglist.ui.confirmation

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment

const val TITLE_KEY = "title_key"
const val MESSAGE_KEY = "message_key"
const val CONFIRM_BUTTON_KEY = "confirm_key"

class ConfirmationDialogFragment : DialogFragment() {

    var confirmAction: () -> Unit = {}
    var declineAction: () -> Unit = {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title = arguments?.getString(TITLE_KEY)
        val message = arguments?.getString(MESSAGE_KEY)
        val confirmButton = arguments?.getString(CONFIRM_BUTTON_KEY) ?: getString(android.R.string.ok)

        return AlertDialog.Builder(requireContext()).apply {
            title?.let { setTitle(it) }
            message?.let { setMessage(it) }
            setPositiveButton(confirmButton) { _, _ -> confirmAction.invoke() }
            setNegativeButton(android.R.string.cancel) { _, _ -> declineAction.invoke() }
        }.create()
    }

    companion object {
        const val TAG = "ConfirmationDialogFragment"

        fun newInstance(
            title: String,
            message: String? = null,
            confirmButton: String? = null
        ): ConfirmationDialogFragment {
            return ConfirmationDialogFragment().apply {
                arguments = bundleOf(
                    TITLE_KEY to title,
                    MESSAGE_KEY to message,
                    CONFIRM_BUTTON_KEY to confirmButton
                )
            }
        }
    }
}