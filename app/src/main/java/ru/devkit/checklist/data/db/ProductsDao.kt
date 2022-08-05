package ru.devkit.checklist.data.db

import androidx.room.*
import ru.devkit.checklist.data.model.Product

@Dao
interface ProductsDao {

    @Query("SELECT * FROM PRODUCT")
    fun getAll(): List<Product>

    @Query("SELECT * FROM PRODUCT WHERE name = :name")
    fun getByKey(name: String): Product?

    @Insert
    fun insert(product: Product)

    @Update
    fun update(product: Product)

    @Delete
    fun delete(product: Product)

    @Query("DELETE FROM Product")
    fun clear()
}