package ru.devkit.shoppinglist

import android.app.Application
import androidx.room.Room
import ru.devkit.shoppinglist.data.db.ProductsDatabase

class App : Application() {
    val database: ProductsDatabase by lazy {
        Room.databaseBuilder(this, ProductsDatabase::class.java, ProductsDatabase.NAME).build()
    }
}