# Datetime

[Browse code](https://github.com/respawn-app/kmmutils/tree/master/datetime/src/commonMain/kotlin/pro/respawn/kmmutils/datetime)

Dependencies:

* kotlinx.datetime
* JVM 8.0

## Time

A `Time` class is `LocalTime` on steroids.

It's different from LocalTime in the fact that it defines a far more robust API, a lot of useful operators and
functions, and conversions between both JVM LocalTime and kotlinx.datetime types.
Time supports precision up to seconds, and values less than 24 hours.
Time is perfect for persistent storage as a number of seconds or Integer value.

## Extensions

See KDocs for a list of all the extensions available.

This module includes various operators and builders that are (for some reason) missing from the kotlinx.datetime API.
Our opinion on what should be included in the api is not so strict as the original library authors' is,
so these extensions are meant make
the usage of the datetime library easier while still providing __some__ degree of the API safety that the original
library insist on.

### Builders

* `LocalTime.now()`
* `LocalDate.now()`
* `LocalDateTime.now()`
* `Instant.EPOCH`

### Operators

* `Month.length()`
* `LocalDateTime.asMidnight()`
* `LocalDatetime.plus<days / months / years>()`
* `LocalDateTime.withPrevious<dayOfWeek / dayOfMonth / dayOfYear>()` - originally coming from java.time
* `LocalDateTime.withNext<dayOfWeek / dayOfMonth / dayOfYear>()`
* `java.util.Calendar` setters and adjusters

This is not a comprehensive list of available extensions.
