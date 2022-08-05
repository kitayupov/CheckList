package ru.devkit.checklist

import android.app.Application
import ru.devkit.checklist.data.db.ProductsDatabase
import ru.devkit.checklist.data.preferences.PreferencesProvider
import ru.devkit.checklist.data.repository.ProductsRepository
import ru.devkit.checklist.data.source.ProductsDataSource
import ru.devkit.checklist.domain.DataModelStorageInteractor
import ru.devkit.checklist.router.CheckListRouter
import ru.devkit.checklist.ui.presentation.CheckListPresenter

class App : Application() {
    private val database by lazy { ProductsDatabase.getInstance(this) }
    private val dataSource by lazy { ProductsDataSource(database) }
    private val repository by lazy { ProductsRepository(dataSource) }
    private val interactor by lazy { DataModelStorageInteractor(repository) }
    private val preferences by lazy { PreferencesProvider(this) }
    val presenter by lazy { CheckListPresenter(interactor, preferences) }
    val router by lazy { CheckListRouter(this) }
}