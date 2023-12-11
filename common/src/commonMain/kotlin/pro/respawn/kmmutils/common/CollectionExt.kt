@file:OptIn(ExperimentalContracts::class)

package pro.respawn.kmmutils.common

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Returns a random index of this collection
 */
public fun <T> Collection<T>.randomIndex(): Int = indices.random()

/**
 * Creates a copy of [this] with [item] replaced with whatever is returned by [replacement].
 *
 * [item] **MUST** be an item contained in the original list, or you will get an [IndexOutOfBoundsException]
 * @see tryReplace
 * @return a list with all values of [this] with [item] replaced by [replacement]
 */
public inline fun <T> Iterable<T>.replace(item: T, replacement: T.() -> T): List<T> {
    val newList = toMutableList()
    newList[indexOf(item)] = replacement(item)
    return newList
}

/**
 * @return null if the collection is empty, else ther result of [single]
 */
public fun <T> Iterable<T>.singleOrNullIfEmpty(): T? = if (none()) null else single()

/**
 * Fills the list with **shallow** copies of the receiver
 */
public fun <T> T.copies(count: Int): List<T> {
    val list = mutableListOf<T>()
    repeat(count) {
        list.add(this)
    }
    return list
}

/**
 * Like [indexOfFirst] but returns null instead of -1. If the not found.
 */
public inline fun <T> Iterable<T>.indexOfFirstOrNull(predicate: (T) -> Boolean): Int? =
    indexOfFirst(predicate).takeIf { it != -1 }

/**
 * @return a new list, where each item that matches [predicate] is replaced with [with]
 */
public inline fun <T> Iterable<T>.replaceIf(with: T, predicate: (T) -> Boolean): List<T> =
    map { if (predicate(it)) with else it }

/**
 * @param apply a function that is applied on each item that matches [predicate]
 * @return a new list, where each item that matches [predicate] is replaced with the result of applying [apply] to it
 */
public inline fun <T> Iterable<T>.replaceIf(apply: T.() -> T, predicate: (T) -> Boolean): List<T> =
    map { if (predicate(it)) apply(it) else it }

/**
 * Replaces all values of [this] with values from [src]
 */
public fun <T> MutableCollection<T>.replaceWith(src: Collection<T>) {
    clear()
    addAll(src)
}

/**
 * Returns [count] number elements from [this]
 */
public fun <T> Iterable<T>.randomElements(count: Int): List<T> = shuffled().take(count)

/**
 * @param selector is a function using which the value by which we reorder is going to be selected, must be the same
 * value that is specified in the [order]
 * @param order a list of objects that represents the order which should be used to sort the original list
 * @returns A copy of this list ordered according to the order
 */
public inline fun <T, R> Iterable<T>.reorderBy(order: List<R>, crossinline selector: (T) -> R): List<T> {
    // associate the values with indexes and create a map
    val orderMap = order.withIndex().associate { it.value to it.index }
    // sort the habits sorted using the comparator that places values not present in a map last
    // and uses the order of the items in the map as its basis for sorting.
    return sortedWith(compareBy(nullsLast()) { orderMap[selector(it)] }).toMutableList()
}

/**
 * Swaps values [index1] with [index2] in place.
 */
public fun <T> MutableList<T>.swap(index1: Int, index2: Int): MutableList<T> {
    val tmp = this[index1]
    this[index1] = this[index2]
    this[index2] = tmp
    return this
}

/**
 *
 * Returns a shallow copy of this list with the items at [index1] and [index2] swapped.
 */
public fun <T> List<T>.swapped(index1: Int, index2: Int): List<T> {
    val list = toMutableList()
    return list.swap(index1, index2)
}

/**
 * Returns a list of pairs, where each value corresponds to all possible pairings with values from [other].
 * this: A, B, C
 * other: 1, 2
 * this.cartesianProduct(other) = [A to 1, A to 2, B to 1, B to 2, C to 1, C to 2]
 */
public fun <T, R> Iterable<T>.cartesianProduct(other: Iterable<R>): List<Pair<T, R>> =
    flatMap { value -> List(other.count()) { value }.zip(other) }

/**
 * Tries to replace [item] with [replacement] and does nothing if [item] is not found in [this]
 * @returns a list of items with [item] swapped with [replacement]
 */
public inline fun <T> Iterable<T>.tryReplace(
    item: T,
    replacement: T.() -> T
): List<T> {
    contract {
        callsInPlace(replacement, InvocationKind.AT_MOST_ONCE)
    }
    val i = indexOf(item)
    if (i == -1) return this.toList()

    val newList = toMutableList()
    newList[i] = replacement(item)
    return newList
}

/**
 * Will first put [this]'s elements into chunks of size [chunkSize], then calculate
 * an [average] of each of the chunks.
 *
 */
public fun Sequence<Float>.chunkedAverage(chunkSize: Int): Sequence<Double> = ifEmpty { emptySequence() }
    .chunked(chunkSize.coerceAtLeast(1)) { it.average() }

/**
 * Will first put [this]'s elements into chunks of size [chunkSize], then calculate
 * an [average] of each of the chunks.
 */
public fun Collection<Float>.chunkedAverage(chunkSize: Int): List<Double> = ifEmpty { return emptyList() }
    .chunked(chunkSize.coerceAtLeast(1)) { it.average() }

/**
 * Filter [this] by searching for elements that contain [substring],
 * or if string is not [String.isValid], the list itself
 * @param substring a string, that must be [String.isValid]
 * @return a resulting list
 */
public fun Iterable<String>.filterBySubstring(
    substring: String?,
    ignoreCase: Boolean = false
): List<String> = if (!substring.isValid) toList() else asSequence()
    .filter { it.contains(substring!!, ignoreCase) }
    .toList()
