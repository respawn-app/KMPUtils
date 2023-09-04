# KMPUtils

[![CI](https://github.com/respawn-app/kmmutils/actions/workflows/ci.yml/badge.svg?branch=master)](https://github.com/respawn-app/kmmutils/actions/workflows/ci.yml)
![Docs](https://img.shields.io/website?down_color=red&label=Docs&up_color=green&up_message=Online&url=http%3A%2F%2Fopensource.respawn.pro%2Fkmmutils%2F%23%2F)
[![CodeFactor](https://www.codefactor.io/repository/github/respawn-app/kmmutils/badge)](https://www.codefactor.io/repository/github/respawn-app/kmmutils)
![GitHub top language](https://img.shields.io/github/languages/top/respawn-app/kmmutils)
![GitHub](https://img.shields.io/github/license/respawn-app/kmmutils)
![GitHub issues](https://img.shields.io/github/issues/respawn-app/kmmutils)
![GitHub last commit](https://img.shields.io/github/last-commit/respawn-app/kmmutils)
[![AndroidWeekly #556](https://androidweekly.net/issues/issue-556/badge)](https://androidweekly.net/issues/issue-556/)

![badge][badge-android] ![badge][badge-jvm] ![badge][badge-js] ![badge][badge-nodejs] ![badge][badge-linux] ![badge][badge-windows] ![badge][badge-ios] ![badge][badge-mac] ![badge][badge-watchos] ![badge][badge-tvos]

KMP Utils is a collection of all the things that are missing from Kotlin STL, popular KMP libraries & platform SDKs.
The library is meant to be a drop-in dependency - no need to study anything - just add and
enjoy the expanded API of the things you are used to, relying on autocompletion to come up with suggestions for you.

See documentation at [https://opensource.respawn.pro/kmputils/](https://opensource.respawn.pro/kmputils/)
Javadocs are at [/kmptils/javadocs](https://opensource.respawn.pro/kmputils/javadocs/)

## ‼️ ApiResult has moved! Find the new repository and migration guide at https://github.com/respawn-app/ApiResult ‼️

### Features

* [InputForms](https://opensource.respawn.pro/kmputils/#/inputforms): A stateful and composable text input field
  validation framework with clean DSL.
* [Common](https://opensource.respawn.pro/kmputils/#/common): Kotlin standard library extensions
* [Datetime](https://opensource.respawn.pro/kmputils/#/datetime): All the things missing from kotlinx.datetime and Java
  Calendar & DateTime API.
* [Coroutines](https://opensource.respawn.pro/kmputils/#/coroutines): Things missing from Coroutines & Flows API.

## Installation

![Maven Central](https://img.shields.io/maven-central/v/pro.respawn.kmmutils/apiresult?label=Maven%20Central)

```toml
[versions]
kmmutils = "< Badge above 👆🏻 >"

[dependencies]
kmmutils-common = { module = "pro.respawn.kmmutils:common", version.ref = "kmmutils" }
kmmutils-datetime = { module = "pro.respawn.kmmutils:datetime", version.ref = "kmmutils" }
kmmutils-coroutines = { module = "pro.respawn.kmmutils:coroutines", version.ref = "kmmutils" }
kmmutils-inputforms = { module = "pro.respawn.kmmutils:inputforms", version.ref = "kmmutils" }

[bundles]
kmmutils = [
    "kmmutils-common",
    "kmmutils-datetime",
    "kmmutils-coroutines",
    "kmmutils-inputforms"
]
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
