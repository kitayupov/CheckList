package ru.devkit.shoppinglist.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.devkit.shoppinglist.data.model.Product

const val NAME = "Products"

@Database(entities = [Product::class], version = 1)
abstract class ProductsDatabase : RoomDatabase() {

    abstract fun productsDao(): ProductsDao

    companion object {
        @Volatile
        private var INSTANCE: ProductsDatabase? = null

        fun getInstance(context: Context): ProductsDatabase {
            if (INSTANCE != null) return INSTANCE!!

            synchronized(this) {
                INSTANCE = Room.databaseBuilder(context, ProductsDatabase::class.java, NAME)
                    .fallbackToDestructiveMigration()
                    .build()

                return INSTANCE!!
            }
        }
    }
}