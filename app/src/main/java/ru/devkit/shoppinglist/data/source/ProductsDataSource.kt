package ru.devkit.shoppinglist.data.source

import ru.devkit.shoppinglist.data.db.ProductsDatabase
import ru.devkit.shoppinglist.data.model.Product

class ProductsDataSource(
    productsDatabase: ProductsDatabase
) : DataSource<Product> {

    private val dao = productsDatabase.productsDao()

    override fun getItems(): List<Product> {
        return dao.getAll()
    }

    override fun create(elem: Product) {
        val existed = dao.getByName(elem.name)
        if (existed == null) {
            dao.insert(elem)
        } else {
            dao.update(elem)
        }
    }

    override fun update(elem: Product) {
        dao.update(elem)
    }

    override fun delete(elem: Product) {
        dao.delete(elem)
    }
}