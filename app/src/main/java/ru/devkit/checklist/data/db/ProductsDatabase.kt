package ru.devkit.checklist.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.devkit.checklist.data.model.Product

private const val DATABASE_NAME = "Products"

@Database(entities = [Product::class], version = 2)
abstract class ProductsDatabase : RoomDatabase() {

    abstract fun productsDao(): ProductsDao

    companion object {
        @Volatile
        private var INSTANCE: ProductsDatabase? = null

        fun getInstance(context: Context): ProductsDatabase {
            if (INSTANCE != null) return INSTANCE!!

            synchronized(this) {
                INSTANCE = Room.databaseBuilder(context, ProductsDatabase::class.java, DATABASE_NAME)
                    .addMigrations(MIGRATION_1_2)
                    .build()

                return INSTANCE!!
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN ranking INTEGER DEFAULT 0 NOT NULL")
            }
        }
    }
}