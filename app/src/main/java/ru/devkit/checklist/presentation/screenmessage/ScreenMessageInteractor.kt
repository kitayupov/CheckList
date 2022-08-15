package ru.devkit.checklist.presentation.screenmessage

import android.content.Context
import android.widget.Toast
import androidx.annotation.UiContext

class ScreenMessageInteractor(
    @UiContext private val context: Context
) {

    fun showMessage(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}