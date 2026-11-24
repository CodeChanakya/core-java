#!/usr/bin/env bash
# Compiles the code and its tests, then runs the tests with JUnit 5.
#   ./run.sh           run the tests and show the full report
#   ./run.sh --check   same, but only print a one-line verdict (used by CI)
#
# JUnit is a single jar. This script downloads it once into lib/ (which Git
# ignores), so you do not need Maven or an IDE yet. Phase 9 does it the
# professional way with Maven.
set -euo pipefail
cd "$(dirname "$0")"

JUNIT_VERSION="1.11.4"
JAR="lib/junit-platform-console-standalone-${JUNIT_VERSION}.jar"
URL="https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/${JUNIT_VERSION}/junit-platform-console-standalone-${JUNIT_VERSION}.jar"

if [ ! -f "$JAR" ]; then
    echo "Downloading JUnit ${JUNIT_VERSION} (one time only)..."
    mkdir -p lib
    curl -fsSL -o "$JAR" "$URL" || { rm -f "$JAR"; echo "Download failed. Check your internet connection."; exit 1; }
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
