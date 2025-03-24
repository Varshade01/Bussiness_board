package com.example.code_gen.utils

/**
 * Checks if [collection] has duplicate entries using [selector].
 */
internal fun <T, K> hasDuplicates(collection: Collection<T>, selector: (T) -> K): Boolean {
    val set = collection.distinctBy(selector)
    return set.size < collection.size
}