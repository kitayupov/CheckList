package ru.devkit.checklist.data.preferences

import android.content.Context
import android.content.SharedPreferences
import ru.devkit.checklist.domain.SortType

private const val NAME = "ru.devkit.checklist"

const val EXPAND_COMPLETED_KEY = "expand_completed"
const val SORT_TYPE_KEY = "sort_type"

class PreferencesProvider(context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    fun readExpandCompleted(): Boolean = getBoolean(EXPAND_COMPLETED_KEY, true)

    fun writeExpandCompleted(expanded: Boolean) = putBoolean(EXPAND_COMPLETED_KEY, expanded)

    fun readSortType() = SortType.fromString(getString(SORT_TYPE_KEY, SortType.DEFAULT.name))

    fun writeSortType(sortType: SortType) = putString(SORT_TYPE_KEY, sortType.name)

    private fun getBoolean(name: String, default: Boolean = false) = preferences.getBoolean(name, default)

    private fun putBoolean(name: String, value: Boolean) = preferences.edit().putBoolean(name, value).apply()

    private fun getString(name: String, default: String = "") = preferences.getString(name, default) ?: default

    private fun putString(name: String, value: String) = preferences.edit().putString(name, value).apply()
}