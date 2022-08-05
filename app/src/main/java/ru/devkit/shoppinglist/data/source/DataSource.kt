package ru.devkit.shoppinglist.data.source

interface DataSource<T, V> {
    fun getItems(): List<T>
    fun getItemWithKey(key: V): T?
    fun create(elem: T)
    fun update(elem: T)
    fun delete(elem: T)
    fun clear()
}