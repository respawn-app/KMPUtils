# Common

[Browse code](https://github.com/respawn-app/kmmutils/tree/master/common/src)

Includes many small quality of life improvements to the Kotlin standard library.

* Conversion operators from primitives
* `String.isValid` and related extensions for quick validation
* `fastLazy`
* Number signs
* `Number.takeIfNotZero()` and other extensions
* `Number.takeIfFinite()`

### Collections

* `replace()` and `tryReplace()` - for quickly replacing a value in a collection
* `cartesianProduct()`
* `swap()` - why isn't this in the Kotlin STL?
* `Range.size`
* `Range.midpoint`
* `avg()` - computes an average of values
* `Range.expand(other: Range)` - makes a range that is the maximum of the two specified ranges
* New sorting methods

This is not a comprehensive list of extensions as new ones may be added later.
