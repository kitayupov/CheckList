package ru.devkit.checklist.common

import android.content.Context
import android.content.res.Resources
import android.content.res.Resources.NotFoundException
import androidx.annotation.StringRes

class ResourceProvider(private val context: Context) {

    private fun resources(): Resources {
        return context.resources
    }

    @Throws(NotFoundException::class)
    fun getString(@StringRes stringId: Int): String {
        return resources().getString(stringId)
    }

    @Throws(NotFoundException::class)
    fun getString(@StringRes stringId: Int, vararg formatArgs: Any?): String {
        return resources().getString(stringId, *formatArgs)
    }
}