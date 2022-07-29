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
import ru.devkit.shoppinglist.ui.presentation.ShoppingListContract
import ru.devkit.shoppinglist.ui.presentation.ShoppingListPresenter

class MainActivity : AppCompatActivity() {

    private var count = 0

    private val presenter = ShoppingListPresenter(ShoppingListRepository())

    private val adapter = ShoppingListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(decoration)
        recyclerView.adapter = adapter

        val floatingActionButton = findViewById<View>(R.id.floating_button)
        floatingActionButton.setOnClickListener {
            presenter.addItem(ShoppingListItemModel("new item ${count++}"))
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(MvpViewImpl())
    }

    override fun onPause() {
        super.onPause()
        presenter.detachView()
    }

    inner class MvpViewImpl : ShoppingListContract.MvpView {
        override fun showItems(list: List<ShoppingListItemModel>) {
            adapter.updateData(list)
        }
    }
}