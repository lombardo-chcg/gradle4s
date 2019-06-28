#!/bin/bash

[ ! -d build/classes ] && { echo "compiling..."; ./gradlew classes --quiet; }
echo "running..."
./gradlew run --console=plain --quiet