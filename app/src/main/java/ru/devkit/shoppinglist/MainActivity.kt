package ru.devkit.shoppinglist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import ru.devkit.shoppinglist.data.db.ProductsDatabase
import ru.devkit.shoppinglist.data.model.ListItemDataModel
import ru.devkit.shoppinglist.data.preferences.PreferencesProvider
import ru.devkit.shoppinglist.data.repository.ProductsRepository
import ru.devkit.shoppinglist.data.source.ProductsDataSource
import ru.devkit.shoppinglist.domain.DataModelStorageInteractor
import ru.devkit.shoppinglist.ui.adapter.ShoppingListAdapter
import ru.devkit.shoppinglist.ui.additem.CreateNewItemViewRouter
import ru.devkit.shoppinglist.ui.confirmation.ConfirmationDialogFragment
import ru.devkit.shoppinglist.ui.model.ListItemUiModel
import ru.devkit.shoppinglist.ui.presentation.ShoppingListContract
import ru.devkit.shoppinglist.ui.presentation.ShoppingListPresenter

class MainActivity : AppCompatActivity() {

    private val createNewItemViewRouter = CreateNewItemViewRouter(supportFragmentManager)
    private lateinit var presenter: ShoppingListPresenter
    private val adapter = ShoppingListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()
        setupActionButton()
        createStorage()
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(MvpViewImpl())
    }

    override fun onPause() {
        super.onPause()
        presenter.detachView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_actions, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_actions_clear_data -> {
                showDialogForClearDataResponse()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun showDialogForClearDataResponse() {
        val dialog = ConfirmationDialogFragment.newInstance(getString(R.string.dialog_clear_data_title))
        dialog.confirmAction = presenter::clearData
        dialog.show(supportFragmentManager, ConfirmationDialogFragment.TAG)
    }

    private fun createStorage() {
        val database = ProductsDatabase.getInstance(this)
        val dataSource = ProductsDataSource(database)
        val repository = ProductsRepository(dataSource)
        val interactor = DataModelStorageInteractor(repository)
        val preferences = PreferencesProvider(this)
        presenter = ShoppingListPresenter(interactor, preferences)
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(decoration)
        recyclerView.adapter = adapter
        adapter.checkedAction = { presenter.updateItem(it) }
        adapter.expandAction = { presenter.expandArchived(it) }
    }

    private fun setupActionButton() {
        val floatingActionButton = findViewById<View>(R.id.floating_button)
        floatingActionButton.setOnClickListener {
            createNewItemViewRouter.showCreateNewItemView() {
                presenter.addItem(ListItemDataModel(it))
            }
        }
    }

    inner class MvpViewImpl : ShoppingListContract.MvpView {
        override fun showItems(list: List<ListItemUiModel>) {
            adapter.updateData(list)
        }
    }
}