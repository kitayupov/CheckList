package ru.devkit.checklist.data.preferences

import android.content.Context
import android.content.SharedPreferences

private const val NAME = "ru.devkit.checklist"

class PreferencesProvider(context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    fun getBoolean(name: String, defValue: Boolean = false) = preferences.getBoolean(name, defValue)

    fun putBoolean(name: String, value: Boolean) = preferences.edit().putBoolean(name, value).apply()

    fun getString(name: String, defValue: String = "") = preferences.getString(name, defValue) ?: defValue

    fun putString(name: String, value: String) = preferences.edit().putString(name, value).apply()

    companion object {
        const val EXPAND_COMPLETED_KEY = "expand_completed"
        const val SORT_TYPE_KEY = "sort_type"
    }
}