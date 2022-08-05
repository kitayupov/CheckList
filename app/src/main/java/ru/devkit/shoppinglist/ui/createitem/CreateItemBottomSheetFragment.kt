package ru.devkit.shoppinglist.ui.createitem

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.devkit.shoppinglist.R

class CreateItemBottomSheetFragment : BottomSheetDialogFragment() {

    var createAction: (String) -> Unit = {}
    var dismissAction: () -> Unit = {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.create_item_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val editText = view.findViewById<EditText>(R.id.input_text)
        val createButton = view.findViewById<View>(R.id.create_button)
        createButton.setOnClickListener {
            val text = editText.text.trim().takeIf(CharSequence::isNotBlank) ?: return@setOnClickListener
            createAction.invoke(text.toString())
            editText.text = null
        }
        editText.requestFocus()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        dismissAction.invoke()
    }

    companion object {
        const val TAG = "CreateItemBottomSheetFragment"
    }
}