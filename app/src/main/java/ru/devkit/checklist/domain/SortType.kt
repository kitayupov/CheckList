package ru.devkit.checklist.domain

enum class SortType {
    DEFAULT,
    RANKING,
    NAME;

    companion object {
        fun fromString(value: String): SortType {
            return values().firstOrNull { it.name == value } ?: DEFAULT
        }
    }
}