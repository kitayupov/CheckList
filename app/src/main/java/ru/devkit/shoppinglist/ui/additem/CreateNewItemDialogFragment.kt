package ru.devkit.shoppinglist.ui.additem

import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ru.devkit.shoppinglist.R

class CreateNewItemDialogFragment : DialogFragment() {

    private val editText: EditText? by lazy { dialog?.findViewById(R.id.input_text) }

    var action: (String) -> Unit = {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.add_item_title)
            .setView(R.layout.dialog_add_item)
            .setPositiveButton(android.R.string.ok) { _, _ -> getText() }
            .setNegativeButton(android.R.string.cancel, null)
            .create()
    }

    private fun getText() {
        val text = editText?.text?.takeIf { it.isNotBlank() } ?: return
        action.invoke(text.toString())
    }

    companion object {
        const val TAG = "CreateNewItemDialogFragment"
    }
}