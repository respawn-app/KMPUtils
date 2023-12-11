# KMPUtils

[![CI](https://github.com/respawn-app/KMPUtils/actions/workflows/ci.yml/badge.svg?branch=master)](https://github.com/respawn-app/KMPUtils/actions/workflows/ci.yml)
![Docs](https://img.shields.io/website?down_color=red&label=Docs&up_color=green&up_message=Online&url=http%3A%2F%2Fopensource.respawn.pro%2FKMPUtils%2F%23%2F)
[![CodeFactor](https://www.codefactor.io/repository/github/respawn-app/KMPUtils/badge)](https://www.codefactor.io/repository/github/respawn-app/KMPUtils)
![GitHub top language](https://img.shields.io/github/languages/top/respawn-app/KMPUtils)
![GitHub](https://img.shields.io/github/license/respawn-app/KMPUtils)
![GitHub issues](https://img.shields.io/github/issues/respawn-app/KMPUtils)
![GitHub last commit](https://img.shields.io/github/last-commit/respawn-app/KMPUtils)
[![AndroidWeekly #556](https://androidweekly.net/issues/issue-556/badge)](https://androidweekly.net/issues/issue-556/)

![badge][badge-android] ![badge][badge-jvm] ![badge][badge-js] ![badge][badge-nodejs] ![badge][badge-linux] ![badge][badge-windows] ![badge][badge-ios] ![badge][badge-mac] ![badge][badge-watchos] ![badge][badge-tvos]

KMP Utils is a collection of all the things that are missing from Kotlin STL, popular KMP libraries & platform SDKs.
The library is meant to be a drop-in dependency - no need to study anything - just add and
enjoy the expanded API of the things you are used to, relying on autocompletion to come up with suggestions for you.

See documentation at [https://opensource.respawn.pro/KMPUtils](https://opensource.respawn.pro/KMPUtils)

Javadocs are at [/KMPUtils/javadocs](https://opensource.respawn.pro/KMPUtils/#/javadocs/)

## ‼️ ApiResult has moved! Find the new repository and migration guide at https://github.com/respawn-app/ApiResult ‼️

### Features

* [InputForms](https://opensource.respawn.pro/KMPUtils/#/inputforms): A stateful and composable text input field
  validation framework with clean DSL.
* [Common](https://opensource.respawn.pro/KMPUtils/#/common): Kotlin standard library extensions
* [Datetime](https://opensource.respawn.pro/KMPUtils/#/datetime): All the things missing from kotlinx.datetime and Java
  Calendar & DateTime API.
* [Coroutines](https://opensource.respawn.pro/KMPUtils/#/coroutines): Things missing from Coroutines & Flows API.

## Installation

![Maven Central](https://img.shields.io/maven-central/v/pro.respawn.kmmutils/apiresult?label=Maven%20Central)

```toml
[versions]
kmputils = "< Badge above 👆🏻 >"

[dependencies]
kmputils-common = { module = "pro.respawn.kmmutils:common", version.ref = "kmputils" }
kmputils-datetime = { module = "pro.respawn.kmmutils:datetime", version.ref = "kmputils" }
kmputils-coroutines = { module = "pro.respawn.kmmutils:coroutines", version.ref = "kmputils" }
kmputils-inputforms = { module = "pro.respawn.kmmutils:inputforms", version.ref = "kmputils" }

[bundles]
kmputils = [
    "kmputils-common",
    "kmputils-datetime",
    "kmputils-coroutines",
    "kmputils-inputforms"
]
```

```kotlin
dependencies {
    val kmputils = "< version badge above 👆🏻 >"
    commonMainImplementation("pro.respawn.kmmutils:common:$kmputils")
    commonMainImplementation("pro.respawn.kmmutils:datetime:$kmputils")
    commonMainImplementation("pro.respawn.kmmutils:coroutines:$kmputils")
    commonMainImplementation("pro.respawn.kmmutils:inputforms:$kmputils")
}
```

## License

```
Copyright 2022-2023 Respawn Team and contributors

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

```

### Name Change notice

Since the "KMM" abbreviation was deprecated by JetBrains, the repository has been renamed to "KMPUtils" and all the associated urls were changed. Unfortunately there is no way to change the group ID of the library without breaking the resolution of dependencies, so the group ID will stay the same for now. 


[badge-android]: http://img.shields.io/badge/-android-6EDB8D.svg?style=flat

[badge-android-native]: http://img.shields.io/badge/support-[AndroidNative]-6EDB8D.svg?style=flat

[badge-jvm]: http://img.shields.io/badge/-jvm-DB413D.svg?style=flat

[badge-js]: http://img.shields.io/badge/-js-F8DB5D.svg?style=flat

[badge-js-ir]: https://img.shields.io/badge/support-[IR]-AAC4E0.svg?style=flat

[badge-nodejs]: https://img.shields.io/badge/-nodejs-68a063.svg?style=flat

[badge-linux]: http://img.shields.io/badge/-linux-2D3F6C.svg?style=flat

[badge-windows]: http://img.shields.io/badge/-windows-4D76CD.svg?style=flat

[badge-wasm]: https://img.shields.io/badge/-wasm-624FE8.svg?style=flat

[badge-apple-silicon]: http://img.shields.io/badge/support-[AppleSilicon]-43BBFF.svg?style=flat

[badge-ios]: http://img.shields.io/badge/-ios-CDCDCD.svg?style=flat

[badge-mac]: http://img.shields.io/badge/-macos-111111.svg?style=flat

[badge-watchos]: http://img.shields.io/badge/-watchos-C0C0C0.svg?style=flat

[badge-tvos]: http://img.shields.io/badge/-tvos-808080.svg?style=flat
