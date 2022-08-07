package ru.devkit.checklist.data.db

import androidx.room.*
import ru.devkit.checklist.data.model.Product

const val TABLE_NAME = "PRODUCT"

@Dao
interface ProductsDao {

    @Query("SELECT * FROM $TABLE_NAME")
    suspend fun getAll(): List<Product>

    @Query("SELECT * FROM $TABLE_NAME WHERE name = :name")
    suspend fun getByKey(name: String): Product?

    @Insert
    suspend fun insert(product: Product)

    @Update
    suspend fun update(product: Product)

    @Delete
    suspend fun delete(product: Product)

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun clear()
}