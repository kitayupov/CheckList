package ru.devkit.shoppinglist.data.db

import androidx.room.*
import ru.devkit.shoppinglist.data.model.Product

@Dao
interface ProductsDao {

    @Query("SELECT * FROM PRODUCT")
    fun getAll(): List<Product>

    @Query("SELECT * FROM PRODUCT WHERE name = :name")
    fun getByName(name: String): Product?

    @Insert
    fun insert(product: Product)

    @Update
    fun update(product: Product)

    @Delete
    fun delete(product: Product)
}