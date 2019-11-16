#!/usr/bin/env bash
rm -rf build
./gradlew assemble
cf target -o $ORG -s backend
cf v3-zdt-push consumer -b java_buildpack -p build/libs/consumer-1.0.0.jar
cf logs consumer
