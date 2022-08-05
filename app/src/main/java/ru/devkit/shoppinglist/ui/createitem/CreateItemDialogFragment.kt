package ru.devkit.shoppinglist.ui.createitem

import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ru.devkit.shoppinglist.R

class CreateItemDialogFragment : DialogFragment() {

    private val editText: EditText? by lazy { dialog?.findViewById(R.id.input_text) }

    var createAction: (String) -> Unit = {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.create_item_title)
            .setView(R.layout.dialog_create_item)
            .setPositiveButton(R.string.create_item_button) { _, _ -> getText() }
            .setNegativeButton(android.R.string.cancel, null)
            .create()
    }

    private fun getText() {
        val text = editText?.text?.trim()?.takeIf { it.isNotBlank() } ?: return
        createAction.invoke(text.toString())
    }

    companion object {
        const val TAG = "CreateItemDialogFragment"
    }
}