package ru.devkit.shoppinglist.ui.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.devkit.shoppinglist.R
import ru.devkit.shoppinglist.data.model.ShoppingListItemModel
import ru.devkit.shoppinglist.ui.adapter.ShoppingListAdapter
import ru.devkit.shoppinglist.data.repository.ShoppingListRepository

class MainActivity : AppCompatActivity() {

    private val repository = ShoppingListRepository()

    private val adapter = ShoppingListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = adapter

        val floatingActionButton = findViewById<View>(R.id.floating_button)
        floatingActionButton.setOnClickListener {
            repository.addItem(ShoppingListItemModel("new item"))
            adapter.list = repository.getItems()
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.list = repository.getItems()
    }
}