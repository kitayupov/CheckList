package ru.devkit.shoppinglist.ui.confirmation

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment

const val TITLE_KEY = "title_key"
const val MESSAGE_KEY = "message_key"

class ConfirmationDialogFragment : DialogFragment() {

    var confirmAction: () -> Unit = {}
    var declineAction: () -> Unit = {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title = arguments?.getString(TITLE_KEY)
        val message = arguments?.getString(MESSAGE_KEY)
        val builder = AlertDialog.Builder(requireContext())
        title?.let { builder.setTitle(it) }
        message?.let { builder.setMessage(it) }
        builder.setPositiveButton(android.R.string.ok) { _, _ -> confirmAction.invoke() }
        builder.setNegativeButton(android.R.string.cancel) { _, _ -> declineAction.invoke() }
        return builder.create()
    }

    companion object {
        const val TAG = "ConfirmationDialogFragment"

        fun newInstance(title: String, message: String = ""): ConfirmationDialogFragment {
            return ConfirmationDialogFragment().apply {
                arguments = bundleOf(
                    TITLE_KEY to title,
                    MESSAGE_KEY to message
                )
            }
        }
    }
}