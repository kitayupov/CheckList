package ru.devkit.checklist.data.source

interface DataSource<T> {
    fun getItems(): List<T>
    fun create(elem: T)
    fun update(elem: T)
    fun delete(elem: T)
    fun clear()
}