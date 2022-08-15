package ru.devkit.checklist.presentation.createitemaction

class CreateItemActionPresenter : CreateItemActionContract.MvpPresenter {

    private var view: CreateItemActionContract.MvpView? = null

    override fun attachView(view: CreateItemActionContract.MvpView) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun showView() {
        view?.show()
    }

    override fun hideView() {
        view?.hide()
    }
}