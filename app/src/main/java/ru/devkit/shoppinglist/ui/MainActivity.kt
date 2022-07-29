package ru.devkit.shoppinglist.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import ru.devkit.shoppinglist.R
import ru.devkit.shoppinglist.data.model.ShoppingListItemModel
import ru.devkit.shoppinglist.data.repository.ShoppingListRepository
import ru.devkit.shoppinglist.ui.adapter.ShoppingListAdapter
import ru.devkit.shoppinglist.ui.additem.CreateNewItemViewRouter
import ru.devkit.shoppinglist.ui.model.ShoppingListItemUiModel
import ru.devkit.shoppinglist.ui.presentation.ShoppingListContract
import ru.devkit.shoppinglist.ui.presentation.ShoppingListPresenter

class MainActivity : AppCompatActivity() {

    private val createNewItemViewRouter = CreateNewItemViewRouter(supportFragmentManager)
    private val presenter = ShoppingListPresenter(ShoppingListRepository())
    private val adapter = ShoppingListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()
        setupActionButton()
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(MvpViewImpl())
    }

    override fun onPause() {
        super.onPause()
        presenter.detachView()
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(decoration)
        recyclerView.adapter = adapter
        adapter.action = { presenter.updateItem(it) }
    }

    private fun setupActionButton() {
        val floatingActionButton = findViewById<View>(R.id.floating_button)
        floatingActionButton.setOnClickListener {
            createNewItemViewRouter.showCreateNewItemView() {
                presenter.addItem(ShoppingListItemModel(it))
            }
        }
    }

    inner class MvpViewImpl : ShoppingListContract.MvpView {
        override fun showItems(list: List<ShoppingListItemUiModel>) {
            adapter.updateData(list)
        }
    }
}