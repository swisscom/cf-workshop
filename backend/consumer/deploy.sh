#!/usr/bin/env bash
rm -rf build
./gradlew assemble
cf target -s backend
cf push -f manifest.yml
