#!/bin/sh

export GRADLE_OPTS="-Dorg.gradle.daemon=true"
./gradlew clean build


