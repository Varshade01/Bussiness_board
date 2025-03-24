package com.rdua.whiteboard.board.manager.operations

/**
 * An entry point of modifying board data using board option function chaining.
 *
 * All transformations are made on a copy of the original list. Then finishOperation() rewrites
 * original list with modified one.
 */
internal fun <T> MutableList<T>.beginOperation(): BoardOperation<T> {
    return BoardOperation(original = this, items = toMutableList())
}

/**
 * Provides operations for board content.
 *
 * Nesting calling board operations on internal list copy will result in issues. Use only one function
 * chain at a time.
 *
 * Example:
 * ```
 * boardContent.beginOperation()
 *      .find {it.id == selectedItem.id }  // Find currently selected item
 *      .replaceItem { item, items ->      // Change its state
 *          item.copy(text = "new text")
 *      }
 *      .find { it.id == otherItem.id }    // Find other item
 *      .remove()                          // Remove it from list
 *      .then { removedItem, items ->      // Do some additional work on it
 *          /* work with removed item */
 *      }.finishOperation()                 // Apply changes to original boardContent
 * ```
 */
internal open class BoardOperation<T>(
    private val original: MutableList<T>,
    private val items: MutableList<T>,
) {
    /**
     * Adds new item to a provided position.
     *
     * Returns [BoardItemOperation] to continue this function chain with a new added item and
     * an updated items list.
     */
    fun add(index: Int, item: T): BoardItemOperation<T> {
        items.add(index, item)
        return BoardItemOperation(original = original, index = index, item = item, items = items)
    }

    /**
     * Adds new item to the back of the list.
     *
     * Returns [BoardItemOperation] to continue this function chain with a new added item and
     * an updated items list.
     */
    fun add(item: T): BoardItemOperation<T> {
        return add(index = items.size, item = item)
    }

    /**
     * Adds new item to the start of the list.
     *
     * Returns [BoardItemOperation] to continue this function chain with a new added item and
     * an updated items list.
     */
    fun addFirst(item: T): BoardItemOperation<T> {
        return add(index = 0, item = item)
    }

    /**
     * Removes item at the specified [index].
     *
     * Returns [BoardItemOperation] to continue this function chain with a removed item and
     * an updated items list. The index for this element will now be -1, because it's no longer in
     * the list. Calling replace operation on it will not do anything.
     */
    fun removeAt(index: Int): BoardItemOperation<T> {
        val item = if (index in 0..items.size) {
            items.removeAt(index)
        } else {
            null
        }
        return BoardItemOperation(original = original, index = -1, item = item, items = items)
    }

    /**
     * Removes a given [item] from the list.
     *
     * Returns [BoardItemOperation] to continue this function chain with a removed item and
     * an updated items list. The index for this element will now be -1, because it's no longer in
     * the list. Calling replace operation on it will not do anything.
     */
    fun remove(item: T): BoardItemOperation<T> {
        val index = items.indexOf(item)
        return removeAt(index)
    }

    /**
     * Removes first item from the list.
     *
     * Returns [BoardItemOperation] to continue this function chain with a removed item and
     * an updated items list. The index for this element will now be -1, because it's no longer in
     * the list. Calling replace operation on it will not do anything.
     */
    fun removeFirst(): BoardItemOperation<T> {
        return removeAt(0)
    }

    /**
     * Removes last item from the list.
     *
     * Returns [BoardItemOperation] to continue this function chain with a removed item and
     * an updated items list. The index for this element will now be -1, because it's no longer in
     * the list. Calling replace operation on it will not do anything.
     */
    fun removeLast(): BoardItemOperation<T> {
        return removeAt(items.lastIndex)
    }

    /**
     * Finds an item using [predicate].
     *
     * Returns [BoardItemOperation] to continue this function chain with a removed item and
     * an updated items list. If an item is not found, [BoardItemOperation] will hold
     * null as its item.
     */
    fun find(predicate: (T) -> Boolean): BoardItemOperation<T> {
        val index = items.indexOfFirst(predicate)
        val item = if (index in 0..items.size) items[index] else null
        return BoardItemOperation(original = original, index = index, item = item, items = items)
    }

    /**
     * Chains additional operation on this [BoardOperation] giving access to current items list.
     */
    fun then(function: (items: MutableList<T>) -> Unit): BoardOperation<T> {
        function(items)
        return this
    }

    /**
     * Terminal operation that writes temporary items list to original list effectively applying all
     * the changes if there were any.
     */
    fun finishOperation() {
        if (!original.contentEquals(items)) {
            original.clear()
            original.addAll(items)
        }
    }

    /**
     * Alternative way to check equality of two lists.
     *
     * Is added to support BoardOperations on a SnapshotStateList, because it will never be equal to
     * a MutableList, even if their contents are the same.
     */
    private fun <T> MutableList<T>.contentEquals(other: MutableList<T>) : Boolean {
        return size == other.size && containsAll(other)
    }
}