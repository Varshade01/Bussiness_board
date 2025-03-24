package com.rdua.whiteboard.board.manager.operations

/**
 * Provides operations for a specific board item.
 *
 * To get [BoardItemOperation] you need to use [BoardOperation] like add, remove or find.
 */
internal class BoardItemOperation<T>(
    private val original: MutableList<T>,
    private val index: Int,
    private val item: T?,
    private val items: MutableList<T>,
) : BoardOperation<T>(original, items) {

    fun add(function: (item: T?, items: MutableList<T>) -> T?): BoardItemOperation<T> {
        val replacement = function(item, items)

        return if (replacement != null) {
            items.add(replacement)
            BoardItemOperation(
                original = original,
                index = index,
                item = replacement,
                items = items
            )
        } else this
    }

    /**
     * Removes an item associated with this [BoardItemOperation].
     */
    fun remove(): BoardItemOperation<T> {
        return removeAt(index)
    }

    /**
     * Replaces this item in the list with a value provided by [function].
     *
     * Ignores this call and returns this [BoardItemOperation] if the item associated with it was null.
     * In other words, you cannot replace a null item.
     */
    fun replaceItem(function: (item: T, items: MutableList<T>) -> T?): BoardItemOperation<T> {
        if (item == null) return this

        val replacement = function(item, items)
        return if (replacement != null && index in 0..items.size) {
            items[index] = replacement
            BoardItemOperation(
                original = original,
                index = index,
                item = replacement,
                items = items
            )
        } else this
    }

    /**
     * Replaces this item in the list with a value provided by [function] if the [condition] is met.
     *
     * Ignores this call and returns this [BoardItemOperation] if the item associated with it was null.
     * In other words, you cannot replace a null item.
     */
    fun replaceItemWithCondition(
        condition: (item: T?, items: MutableList<T>) -> Boolean,
        function: (item: T, items: MutableList<T>) -> T?,
    ): BoardItemOperation<T> {
        return if (condition(item, items)) {
            replaceItem(function)
        } else this
    }

    /**
     * Chains additional operation on this [BoardItemOperation] giving access to current item and
     * items list.
     */
    fun then(function: (item: T?, items: MutableList<T>) -> Unit): BoardItemOperation<T> {
        function(item, items)
        return this
    }

    /**
     * Adds a list of new item to the current list of items [items].
     *
     * [function] returns a [Pair] object, where
     * the first value is a new item that will be used for the next operations,
     * the second value is the updated list of items.
     */
    fun addAll(
        function: (item: T?, items: MutableList<T>) -> Pair<T?, MutableList<T>>?,
    ): BoardItemOperation<T> {
        val replacements: Pair<T?, MutableList<T>>? = function(item, items)

        return BoardItemOperation(
            original = original,
            index = index,
            item = replacements?.first,
            items = replacements?.second ?: items
        )
    }
}
