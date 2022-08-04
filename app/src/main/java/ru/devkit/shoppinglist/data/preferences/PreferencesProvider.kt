package ru.devkit.shoppinglist.data.preferences

import android.content.Context
import android.content.SharedPreferences

private const val NAME = "ru.devkit.checklist"

class PreferencesProvider(context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences(
        NAME,
        Context.MODE_PRIVATE
    )

    fun getBoolean(name: String, defValue: Boolean = false) =
        preferences.getBoolean(name, defValue)

    fun putBoolean(name: String, value: Boolean) =
        preferences.edit().putBoolean(name, value).apply()

    companion object {
        const val EXPAND_ARCHIVED_KEY = "expand_archived"
    }
}