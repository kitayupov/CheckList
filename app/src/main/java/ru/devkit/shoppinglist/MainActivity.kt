package ru.devkit.shoppinglist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import ru.devkit.shoppinglist.model.ShoppingListItemModel
import ru.devkit.shoppinglist.presentation.ShoppingListAdapter

class MainActivity : AppCompatActivity() {

    private val list = mutableListOf(
        ShoppingListItemModel("potato"),
        ShoppingListItemModel("tomato"),
        ShoppingListItemModel("soup"),
        ShoppingListItemModel("banana"),
        ShoppingListItemModel("milk"),
        ShoppingListItemModel("bread"),
    )

    private val adapter = ShoppingListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        adapter.list = list
    }
}