package com.yundin.core.utils

import android.content.Context
import androidx.annotation.StringRes
import javax.inject.Inject

class ResourceProvider @Inject constructor(private val appContext: Context) {
    fun getString(@StringRes id: Int): String {
        val resources = appContext.resources
        return resources.getString(id)
    }

    fun getString(@StringRes id: Int, vararg formatArgs: Any): String {
        val resources = appContext.resources
        return resources.getString(id, *formatArgs)
    }
}
