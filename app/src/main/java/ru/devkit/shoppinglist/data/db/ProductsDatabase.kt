package ru.devkit.shoppinglist.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.devkit.shoppinglist.data.model.Product

@Database(entities = [Product::class], version = 1)
abstract class ProductsDatabase : RoomDatabase() {
    abstract fun productsDao(): ProductsDao

    companion object {
        const val NAME = "Products"
    }
}