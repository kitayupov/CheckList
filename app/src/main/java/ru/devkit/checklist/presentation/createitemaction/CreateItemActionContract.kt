package ru.devkit.checklist.presentation.createitemaction

object CreateItemActionContract {

    interface MvpView {
        fun show()
        fun hide()
    }

    interface MvpPresenter {
        fun attachView(view: MvpView)
        fun detachView()

        // appearance
        fun showView()
        fun hideView()
    }
}