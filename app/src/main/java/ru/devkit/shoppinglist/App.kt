package ru.devkit.shoppinglist

import android.app.Application
import ru.devkit.shoppinglist.data.db.ProductsDatabase
import ru.devkit.shoppinglist.data.preferences.PreferencesProvider
import ru.devkit.shoppinglist.data.repository.ProductsRepository
import ru.devkit.shoppinglist.data.source.ProductsDataSource
import ru.devkit.shoppinglist.domain.DataModelStorageInteractor
import ru.devkit.shoppinglist.ui.presentation.ShoppingListPresenter

class App : Application() {
    private val database by lazy { ProductsDatabase.getInstance(this) }
    private val dataSource by lazy { ProductsDataSource(database) }
    private val repository by lazy { ProductsRepository(dataSource) }
    private val interactor by lazy { DataModelStorageInteractor(repository) }
    private val preferences by lazy { PreferencesProvider(this) }
    val presenter by lazy { ShoppingListPresenter(interactor, preferences) }
}