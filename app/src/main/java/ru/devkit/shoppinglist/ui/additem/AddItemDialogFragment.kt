package ru.devkit.shoppinglist.ui.additem

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ru.devkit.shoppinglist.R

class AddItemDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(R.layout.dialog_add_item)
        builder.setPositiveButton(getString(R.string.add_item_ok)) { _, _ -> }
        builder.setNegativeButton(getString(R.string.add_item_cancel)) { _, _ -> }
        return builder.create()
    }

    companion object {
        const val TAG = "AddItemDialogFragment"
    }
}