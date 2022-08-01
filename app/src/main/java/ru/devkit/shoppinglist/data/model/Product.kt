package ru.devkit.shoppinglist.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(
    @PrimaryKey
    val id: Long,
    val name: String,
    val checked: Boolean
)