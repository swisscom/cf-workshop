#!/usr/bin/env bash
rm -rf build
./gradlew assemble
cf target -s team-00
cf push -f manifest.yml
