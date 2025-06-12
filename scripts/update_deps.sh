#!/bin/bash

GRADLE_VER="$1"

if [ -z "$GRADLE_VER" ]; then
    GRADLE_VER="latest"
fi

./gradlew wrapper --gradle-version "$GRADLE_VER" --distribution-type bin
./gradlew wrapper --gradle-version "$GRADLE_VER" --distribution-type bin
./gradlew versionCatalogUpdate --no-configuration-cache
