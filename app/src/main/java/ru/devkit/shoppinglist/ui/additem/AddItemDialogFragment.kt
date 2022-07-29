package ru.devkit.shoppinglist.ui.additem

import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ru.devkit.shoppinglist.R

class AddItemDialogFragment : DialogFragment() {

    private val editText: EditText? by lazy { dialog?.findViewById(R.id.input_text) }

    var action: (String) -> Unit = {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.add_item_title)
        builder.setView(R.layout.dialog_add_item)
        builder.setPositiveButton(getString(R.string.add_item_ok)) { _, _ -> getText() }
        builder.setNegativeButton(getString(R.string.add_item_cancel), null)
        return builder.create()
    }

    private fun getText() {
        val text = editText?.text?.takeIf { it.isNotBlank() } ?: return
        action.invoke(text.toString())
    }

    companion object {
        const val TAG = "AddItemDialogFragment"
    }
}