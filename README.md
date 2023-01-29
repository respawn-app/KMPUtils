# kmmutils

This repository is Work In Progress ⚙️

# Contributing

To build the project, you will need the following in your local.properties:

```properties
sdk.dir=/Users/nek/Library/Android/Sdk
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
