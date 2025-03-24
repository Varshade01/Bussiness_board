package com.rdua.whiteboard.common.provider

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * API for providing resources in classes that don't have access to them. Is intended to be used in
 * ViewModels or in domain layer, when resources are required for uses other than UI.
 */
class ResourceProviderImpl @Inject constructor(
    @ApplicationContext val context: Context,
) : ResourceProvider {
    override fun getString(@StringRes id: Int): String = context.getString(id)
    override fun getString(@StringRes id: Int, vararg formatArgs:Any): String = context.getString(id, *formatArgs)
}