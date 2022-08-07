package ru.devkit.checklist.data.source

interface DataSource<T> {
    suspend fun getItems(): List<T>
    suspend fun create(elem: T)
    suspend fun update(elem: T)
    suspend fun delete(elem: T)
    suspend fun clear()
}