# KMM Utilities

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/164d19d053f545abb023ca73d09b6def)](https://app.codacy.com/gh/respawn-app/kmmutils?utm_source=github.com&utm_medium=referral&utm_content=respawn-app/kmmutils&utm_campaign=Badge_Grade_Settings)

This repository is Work In Progress ⚙️

# Contributing

To build the project, you will need the following in your local.properties:

```properties
sdk.dir=...
# only required for publishing
sonatypeUsername=...
sonatypePassword=...
signing.keyId=...
signing.password=...
# --- 
release=false
```

Make sure you have proper plugins installed:

* Detekt
* Kotest
* Kotlin Multiplatform (run `kdoctor` to verify proper setup)
* Compose

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
