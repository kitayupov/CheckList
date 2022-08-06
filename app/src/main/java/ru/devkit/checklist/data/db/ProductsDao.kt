package ru.devkit.checklist.data.db

import androidx.room.*
import ru.devkit.checklist.data.model.Product

const val TABLE_NAME = "PRODUCT"

@Dao
interface ProductsDao {

    @Query("SELECT * FROM $TABLE_NAME")
    fun getAll(): List<Product>

    @Query("SELECT * FROM $TABLE_NAME WHERE name = :name")
    fun getByKey(name: String): Product?

    @Insert
    fun insert(product: Product)

    @Update
    fun update(product: Product)

    @Delete
    fun delete(product: Product)

    @Query("DELETE FROM $TABLE_NAME")
    fun clear()
}