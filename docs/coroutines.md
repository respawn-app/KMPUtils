# Coroutines

A successor to AndroidUtils' `coroutines-ktx` that is in the process of migration.

Includes various extensions for the kotlinx.coroutines library.

## Extensions

* `forEachParallel`- iterate over a collection in parallel
* `mapParallel` - map a collection in parallel
* `launchCatching` - launch a coroutines and catch any exceptions safely, including nested coroutines
* `Flow<Iterable<*>>.mapValues()` - never write `Flow.map { it.map { } }` again
