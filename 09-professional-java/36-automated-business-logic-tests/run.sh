#!/usr/bin/env bash
# Compiles the code and its tests, then runs the tests with JUnit 5.
#   ./run.sh           run the tests and show the full report
#   ./run.sh --check   same, but only print a one-line verdict (used by CI)
#   ./run.sh --maven   run the tests with Maven instead (needs `mvn` installed)
#
# JUnit is a single jar. This script downloads it once into lib/ (which Git
# ignores), so you do not need Maven or an IDE for this route. The --maven
# route is how professional projects do it: pom.xml lists JUnit as a dependency.
set -euo pipefail
cd "$(dirname "$0")"

if [ "${1:-}" = "--maven" ]; then
    exec mvn -B test
fi

JUNIT_VERSION="1.11.4"
JAR="lib/junit-platform-console-standalone-${JUNIT_VERSION}.jar"
PATH_IN_REPO="org/junit/platform/junit-platform-console-standalone/${JUNIT_VERSION}/junit-platform-console-standalone-${JUNIT_VERSION}.jar"

# Maven Central sometimes answers "429 too many requests" when many people share
# one address, as happens on CI servers. So we try a mirror as well, and retry
# a few times with a pause before giving up.
URLS=(
    "https://repo1.maven.org/maven2/${PATH_IN_REPO}"
    "https://maven-central.storage-download.googleapis.com/maven2/${PATH_IN_REPO}"
)

download_junit() {
    mkdir -p lib
    for attempt in 1 2 3; do
        for url in "${URLS[@]}"; do
            if curl -fsSL -o "$JAR.part" "$url"; then
                mv "$JAR.part" "$JAR"
                return 0
            fi
        done
        echo "Download failed (attempt $attempt of 3). Waiting before retrying..."
        sleep $((attempt * 10))
    done
    rm -f "$JAR.part"
    return 1
}

if [ ! -f "$JAR" ]; then
    echo "Downloading JUnit ${JUNIT_VERSION} (one time only)..."
    download_junit || { echo "Could not download JUnit. Check your internet connection and try again in a minute."; exit 1; }
fi

rm -rf out && mkdir -p out
find src/main/java src/test/java -name "*.java" > out/sources.txt
javac --release 21 -cp "$JAR" -d out @out/sources.txt

if [ "${1:-}" = "--check" ]; then
    java -jar "$JAR" execute --class-path out --scan-class-path --disable-banner --details=summary > out/report.txt 2>&1 \
        && { grep -E "tests (successful|failed)" out/report.txt | tr -d "\r"; echo "OK: all tests passed"; } \
        || { cat out/report.txt; echo "FAIL: see the report above"; exit 1; }
else
    java -jar "$JAR" execute --class-path out --scan-class-path --disable-banner --details=tree
fi
