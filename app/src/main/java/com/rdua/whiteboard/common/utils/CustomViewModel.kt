package com.rdua.whiteboard.common.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.lifecycle.DEFAULT_ARGS_KEY
import androidx.lifecycle.SAVED_STATE_REGISTRY_OWNER_KEY
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryOwner
import dagger.hilt.android.internal.lifecycle.HiltViewModelFactory

const val DEFAULT_ARGS = "default_args"

@Composable
inline fun <reified VM : ViewModel> viewModel(
    key: String,
    args: Parcelable?,
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current)
): VM {

    val context = LocalContext.current
    val application = context.applicationContext as Application
    val savedStateRegistryOwner = LocalSavedStateRegistryOwner.current
    val bundle = args?.let {
        Bundle().apply { putParcelable(DEFAULT_ARGS, args) }
    }
    val factory = createViewModelFactory(
        context = context,
        savedStateRegistryOwner = savedStateRegistryOwner,
        bundle = bundle
    )
    val extras = createDefaultViewModelCreationExtras(
        application = application,
        savedStateRegistryOwner = savedStateRegistryOwner,
        viewModelStoreOwner = viewModelStoreOwner,
        bundle = bundle
    )
    return viewModelStoreOwner.get(VM::class.java, key, factory, extras)
}

@Composable
@PublishedApi
internal fun createViewModelFactory(
    context: Context,
    savedStateRegistryOwner: SavedStateRegistryOwner,
    bundle: Bundle?
): ViewModelProvider.Factory {

    val activity = context.let {
        var ctx = it
        while (ctx is ContextWrapper) {
            if (ctx is Activity) {
                return@let ctx
            }
            ctx = ctx.baseContext
        }
        throw IllegalStateException(
            "Expected an activity context for creating a BoardHiltViewModelFactory for a " +
                    "NavBackStackEntry but instead found: $ctx"
        )
    }
    val delegateFactory = SavedStateViewModelFactory(
        context.applicationContext as Application,
        savedStateRegistryOwner
    )
    return HiltViewModelFactory.createInternal(
        activity,
        savedStateRegistryOwner,
        bundle,
        delegateFactory
    )
}

@PublishedApi
internal fun createDefaultViewModelCreationExtras(
    application: Application,
    savedStateRegistryOwner: SavedStateRegistryOwner,
    viewModelStoreOwner: ViewModelStoreOwner,
    bundle: Bundle?
): CreationExtras {
    return MutableCreationExtras().apply {
        set(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY, application)
        set(SAVED_STATE_REGISTRY_OWNER_KEY, savedStateRegistryOwner)
        set(VIEW_MODEL_STORE_OWNER_KEY, viewModelStoreOwner)
        bundle?.let { set(DEFAULT_ARGS_KEY, it) }
    }
}

@PublishedApi
internal fun <VM : ViewModel> ViewModelStoreOwner.get(
    javaClass: Class<VM>,
    key: String,
    factory: ViewModelProvider.Factory,
    extras: CreationExtras
): VM = ViewModelProvider(this.viewModelStore, factory, extras)[key, javaClass]