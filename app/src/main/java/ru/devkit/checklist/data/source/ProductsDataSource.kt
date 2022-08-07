package ru.devkit.checklist.data.source

import ru.devkit.checklist.data.db.ProductsDatabase
import ru.devkit.checklist.data.model.Product

class ProductsDataSource(
    productsDatabase: ProductsDatabase
) : DataSource<Product> {

    private val dao = productsDatabase.productsDao()

    override suspend fun getItems(): List<Product> {
        return dao.getAll()
    }

    override suspend fun create(elem: Product) {
        val existed = dao.getByKey(elem.name)
        if (existed == null) {
            dao.insert(elem)
        } else {
            dao.update(elem)
        }
    }

    override suspend fun update(elem: Product) {
        dao.update(elem)
    }

    override suspend fun delete(elem: Product) {
        dao.delete(elem)
    }

    override suspend fun clear() {
        dao.clear()
    }
}