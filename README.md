# KMM Utilities

[![CI](https://github.com/respawn-app/kmmutils/actions/workflows/ci.yml/badge.svg?branch=master)](https://github.com/respawn-app/kmmutils/actions/workflows/ci.yml)
![Docs](https://img.shields.io/website?down_color=red&label=Docs&up_color=green&up_message=Online&url=http%3A%2F%2Fopensource.respawn.pro%2Fkmmutils%2F%23%2F)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/8846c438e5534e45b7b70d75ccdc0107)](https://app.codacy.com/gh/respawn-app/kmmutils?utm_source=github.com&utm_medium=referral&utm_content=respawn-app/kmmutils&utm_campaign=Badge_Grade_Settings)
[![CodeFactor](https://www.codefactor.io/repository/github/respawn-app/kmmutils/badge)](https://www.codefactor.io/repository/github/respawn-app/kmmutils)
![GitHub top language](https://img.shields.io/github/languages/top/respawn-app/kmmutils)
![GitHub](https://img.shields.io/github/license/respawn-app/kmmutils)
![GitHub issues](https://img.shields.io/github/issues/respawn-app/kmmutils)
![GitHub last commit](https://img.shields.io/github/last-commit/respawn-app/kmmutils)

KMM Utils is a collection of all the things that are missing from Kotlin STL, popular KMM libraries & platform SDKs.

See documentation at [https://opensource.respawn.pro/kmmutils/](https://opensource.respawn.pro/kmmutils/)

### Features

* [ApiResult](https://opensource.respawn.pro/kmmutils/#/apiresult):A monad for wrapping operations that may fail.
  Similar
  to kotlin.Result, but offers much more.
* [Common](https://opensource.respawn.pro/kmmutils/#/common): Kotlin standard library extensions
* [Datetime](https://opensource.respawn.pro/kmmutils/#/datetime): All the things missing from kotlinx.datetime and Java
  Calendar & DateTime API.
* [Coroutines](https://opensource.respawn.pro/kmmutils/#/coroutines): Things missing from Coroutines & Flows API. Also
  includes platform extensions for Android.

## Declaring dependencies

![Maven Central](https://img.shields.io/maven-central/v/pro.respawn.kmmutils/apiresult?label=Maven%20Central)

```toml

[versions]
kmmutils = "< Badge above 👆🏻 >"

[dependencies]
kmmutils-apiresult = { module = "pro.respawn.kmmutils:apiresult", version.ref = "kmmutils" }
kmmutils-common = { module = "pro.respawn.kmmutils:common", version.ref = "kmmutils" }
kmmutils-datetime = { module = "pro.respawn.kmmutils:datetime", version.ref = "kmmutils" }
kmmutils-coroutines = { module = "pro.respawn.kmmutils:coroutines", version.ref = "kmmutils" }

[bundles]
kmmutils = [
    "kmmutils-apiresult",
    "kmmutils-common",
    "kmmutils-datetime",
    "kmmutils-coroutines"
]

```

### Supported platforms

More will be added soon as all code is multiplatform.
Android, JVM, iOS

## License

```
   Copyright 2022 Respawn Team and contributors

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
