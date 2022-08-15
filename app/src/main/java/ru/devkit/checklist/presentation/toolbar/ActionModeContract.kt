package ru.devkit.checklist.presentation.toolbar

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