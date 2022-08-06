package ru.devkit.checklist.ui.createitem

import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.devkit.checklist.R

class CreateItemBottomSheetFragment : BottomSheetDialogFragment() {

    var createAction: (String) -> Unit = {}
    var dismissAction: () -> Unit = {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.create_item_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editText = view.findViewById<EditText>(R.id.input_text)
        editText.setOnKeyListener { _, _, event ->
            if (event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                getText(editText)
            }
            true
        }

        val createButton = view.findViewById<View>(R.id.create_button)
        createButton.setOnClickListener {
            getText(editText)
        }

        editText.requestFocus()
    }

    private fun getText(editText: EditText) {
        val text = editText.text.trim().takeIf(CharSequence::isNotBlank) ?: return
        createAction.invoke(text.toString())
        editText.text = null
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        dismissAction.invoke()
    }

    companion object {
        const val TAG = "CreateItemBottomSheetFragment"
    }
}