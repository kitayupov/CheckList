package ru.devkit.checklist.presentation.toolbar

import ru.devkit.checklist.R
import ru.devkit.checklist.common.ResourceProvider

class ActionModePresenter(
    private val resources: ResourceProvider
) : ActionModeContract.MvpPresenter {

    private var view: ActionModeContract.MvpView? = null

    override fun attachView(view: ActionModeContract.MvpView) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun setSelectionMode(checked: Boolean) {
        view?.setSelectionMode(checked)
    }

    override fun setSelectedCount(count: Int) {
        view?.setTitle(resources.getString(R.string.action_mode_selected_count, count))
    }
}