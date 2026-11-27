#!/usr/bin/env bash
# Compiles and runs this program.
#   ./run.sh             run the demonstration
#   ./run.sh --check     run it and compare the output with expected-output.txt
#   ./run.sh --test      compile and run the JUnit 5 tests
#   ./run.sh --maven     run the demonstration with Maven (needs `mvn`); `mvn test` runs the tests
#
# Two kinds of jar are needed. The Derby database driver is needed to run the program, and the JUnit
# console jar only for --test. Both are downloaded once into lib/ (which Git ignores).
set -euo pipefail
cd "$(dirname "$0")"

if [ "${1:-}" = "--maven" ]; then
    exec mvn -q -B compile exec:java
fi

DERBY_VERSION="10.16.1.1"
JUNIT_VERSION="1.11.4"
# BASE can point at a local folder laid out like Maven Central (used to test this script offline).
BASE="${MAVEN_BASE:-https://repo1.maven.org/maven2}"
MIRROR="https://maven-central.storage-download.googleapis.com/maven2"

# download <path-in-repository> <target-file>
download() {
    local path="$1" target="$2"
    mkdir -p lib
    for attempt in 1 2 3; do
        for root in "$BASE" "$MIRROR"; do
            if curl -fsSL -o "$target.part" "${root}/${path}"; then
                mv "$target.part" "$target"
                return 0
            fi
        done
        echo "Download failed (attempt $attempt of 3). Waiting before retrying..."
        sleep $((attempt * 10))
    done
    rm -f "$target.part"
    return 1
}

need() {
    local path="$1" target="$2"
    if [ ! -f "$target" ]; then
        echo "Downloading $(basename "$target") (one time only)..."
        download "$path" "$target" || { echo "Could not download $(basename "$target"). Check your internet connection and try again in a minute."; exit 1; }
    fi
}

need "org/apache/derby/derby/${DERBY_VERSION}/derby-${DERBY_VERSION}.jar" "lib/derby-${DERBY_VERSION}.jar"
need "org/apache/derby/derbyshared/${DERBY_VERSION}/derbyshared-${DERBY_VERSION}.jar" "lib/derbyshared-${DERBY_VERSION}.jar"

# The class-path separator is ":" on Linux and macOS, and ";" in Git Bash on Windows.
SEP=":"
case "$(uname -s)" in MINGW*|MSYS*|CYGWIN*) SEP=";" ;; esac
DRIVER="lib/derby-${DERBY_VERSION}.jar${SEP}lib/derbyshared-${DERBY_VERSION}.jar"

rm -rf out && mkdir -p out

if [ "${1:-}" = "--test" ]; then
    JUNIT="lib/junit-platform-console-standalone-${JUNIT_VERSION}.jar"
    need "org/junit/platform/junit-platform-console-standalone/${JUNIT_VERSION}/junit-platform-console-standalone-${JUNIT_VERSION}.jar" "$JUNIT"
    find src/main/java src/test/java -name "*.java" > out/sources.txt
    javac --release 21 -cp "$JUNIT" -d out @out/sources.txt
    java -jar "$JUNIT" execute --class-path "out${SEP}${DRIVER}" --scan-class-path --disable-banner --details=summary > out/report.txt 2>&1 \
        && { grep -E "tests (successful|failed)" out/report.txt | tr -d "\r"; echo "OK: all tests passed"; } \
        || { cat out/report.txt; echo "FAIL: see the report above"; exit 1; }
    exit 0
fi

find src/main/java -name "*.java" > out/sources.txt
javac --release 21 -d out @out/sources.txt

if [ "${1:-}" = "--check" ]; then
    java -cp "out${SEP}${DRIVER}" com.codechanakya.empdb.Main | tr -d "\r" | diff - expected-output.txt \
        && echo "OK: output matches expected-output.txt"
else
    java -cp "out${SEP}${DRIVER}" com.codechanakya.empdb.Main
fi
