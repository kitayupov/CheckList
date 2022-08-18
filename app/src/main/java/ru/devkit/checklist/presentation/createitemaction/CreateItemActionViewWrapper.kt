package ru.devkit.checklist.presentation.createitemaction

import com.google.android.material.floatingactionbutton.FloatingActionButton

class CreateItemActionViewWrapper(
    private val floatingActionButton: FloatingActionButton,
    private val clickAction: () -> Unit
) : CreateItemActionContract.MvpView {

    init {
        floatingActionButton.setOnClickListener { clickAction.invoke() }
    }

    override fun show() {
        floatingActionButton.show()
    }

    override fun hide() {
        floatingActionButton.hide()
    }
}