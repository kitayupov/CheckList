package ru.devkit.checklist.presentation.actionmode

object ActionModeContract {

    interface MvpView {
        fun setSelectionMode(checked: Boolean)
        fun setTitle(title: String)
    }

    interface MvpPresenter {
        fun attachView(view: MvpView)
        fun detachView()

        fun setSelectionMode(checked: Boolean)
        fun setSelectedCount(count: Int)
    }
}