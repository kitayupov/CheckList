package ru.devkit.checklist

import android.app.Application
import ru.devkit.checklist.common.ResourceProvider
import ru.devkit.checklist.data.db.ProductsDatabase
import ru.devkit.checklist.data.preferences.PreferencesProvider
import ru.devkit.checklist.data.repository.ProductsRepository
import ru.devkit.checklist.data.source.ProductsDataSource
import ru.devkit.checklist.domain.DataModelStorageInteractor
import ru.devkit.checklist.presentation.createitemaction.CreateItemActionPresenter
import ru.devkit.checklist.presentation.screenmessage.ScreenMessageInteractor
import ru.devkit.checklist.presentation.toolbar.ActionModePresenter
import ru.devkit.checklist.router.CheckListRouter
import ru.devkit.checklist.ui.presentation.CheckListPresenter

class App : Application() {
    private val database by lazy { ProductsDatabase.getInstance(this) }
    private val dataSource by lazy { ProductsDataSource(database) }
    private val repository by lazy { ProductsRepository(dataSource) }
    private val storageInteractor by lazy { DataModelStorageInteractor(repository) }
    private val messageInteractor by lazy { ScreenMessageInteractor(this) }
    private val preferences by lazy { PreferencesProvider(this) }
    private val resources by lazy { ResourceProvider(this) }
    val actionModePresenter by lazy { ActionModePresenter(resources) }
    val checkListPresenter by lazy { CheckListPresenter(storageInteractor, messageInteractor, actionModePresenter, preferences, resources) }
    val createItemActionPresenter by lazy { CreateItemActionPresenter() }
    val router by lazy { CheckListRouter(this) }
}