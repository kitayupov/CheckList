package ru.devkit.checklist.data.source

import ru.devkit.checklist.data.db.ProductsDatabase
import ru.devkit.checklist.data.model.Product

class ProductsDataSource(
    productsDatabase: ProductsDatabase
) : DataSource<Product> {

    private val dao = productsDatabase.productsDao()

    override fun getItems(): List<Product> {
        return dao.getAll()
    }

    override fun create(elem: Product) {
        val existed = dao.getByKey(elem.name)
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

    override fun clear() {
        dao.clear()
    }
}