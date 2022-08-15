package ru.devkit.checklist.presentation.createitemaction

import com.google.android.material.floatingactionbutton.FloatingActionButton

class CreateItemActionViewWrapper(
    private val floatingActionButton: FloatingActionButton
) : CreateItemActionContract.MvpView {

    override fun show() {
        floatingActionButton.show()
    }

    override fun hide() {
        floatingActionButton.hide()
    }
}