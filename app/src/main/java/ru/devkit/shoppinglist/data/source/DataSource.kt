package ru.devkit.shoppinglist.data.source

interface DataSource<T> {
    fun getItems(): List<T>
    fun add(item: T)
    fun update(item: T)
    fun remove(item: T)
}