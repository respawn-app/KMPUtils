package pro.respawn.kmmutils.common

import kotlin.random.Random

public fun <T> Collection<T>.randomIndex(): Int = Random.nextInt(this.size)

/**
 * Creates a copy of [this] list with [item] replaced with whatever is returned by [replacement].
 *
 * [item] **MUST** be an item contained in the original list, or you will get an [IndexOutOfBoundsException]
 */
public inline fun <T> List<T>.replace(item: T, replacement: T.() -> T): List<T> {
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

public inline fun <T> Iterable<T>.indexOfFirstOrNull(predicate: (T) -> Boolean): Int? =
    indexOfFirst(predicate).takeIf { it != -1 }

/**
 * @return a new list, where each item that matches [predicate] is replaced with [with]
 */
public inline fun <T> List<T>.replaceIf(with: T, predicate: (T) -> Boolean): List<T> =
    map { if (predicate(it)) with else it }

/**
 * @param apply a function that is applied on each item that matches [predicate]
 * @return a new list, where each item that matches [predicate] is replaced with the result of applying [apply] to it
 */
public inline fun <T> List<T>.replaceIf(apply: T.() -> T, predicate: (T) -> Boolean): List<T> =
    map { if (predicate(it)) apply(it) else it }

public fun <T> MutableCollection<T>.replaceWith(src: Collection<T>) {
    clear()
    addAll(src)
}

public fun <T> List<T>.randomElements(count: Int): List<T> = shuffled().take(count)

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

public fun <T> MutableList<T>.swap(index1: Int, index2: Int): MutableList<T> {
    val tmp = this[index1]
    this[index1] = this[index2]
    this[index2] = tmp
    return this
}

/**
 * Returns a shallow copy of this list with the items swapped
 */
public fun <T> List<T>.swapped(index1: Int, index2: Int): List<T> {
    val list = toMutableList()
    return list.swap(index1, index2)
}

public fun <T, R> Iterable<T>.cartesianProduct(other: Iterable<R>): List<Pair<T, R>> =
    flatMap { value -> List(other.count()) { value }.zip(other) }

public inline fun <T> List<T>.tryReplace(
    item: T,
    replacement: T.() -> T
): List<T> {
    val i = indexOf(item)
    if (i == -1) return this

    val newList = toMutableList()
    newList[i] = replacement(item)
    return newList
}