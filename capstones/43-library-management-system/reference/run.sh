#!/usr/bin/env bash
# Compiles and runs this program.
#   ./run.sh             run the demonstration
#   ./run.sh --check     run it and compare the output with expected-output.txt
#   ./run.sh --test      compile and run all the JUnit 5 tests
#   ./run.sh --grade     run only the acceptance tests and print the automatic part of your score (40 points)
#   ./run.sh --compile   check that main and test code compile; run nothing
#   ./run.sh --maven     run the demonstration with Maven (needs `mvn`); `mvn test` runs the tests
#
# This program needs no database. Only --test, --grade and --compile need a jar: the JUnit console launcher,
# downloaded once into lib/ (which Git ignores).
set -euo pipefail
cd "$(dirname "$0")"

if [ "${1:-}" = "--maven" ]; then
    exec mvn -q -B compile exec:java
fi

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

# The class-path separator is ":" on Linux and macOS, and ";" in Git Bash on Windows.
SEP=":"
case "$(uname -s)" in MINGW*|MSYS*|CYGWIN*) SEP=";" ;; esac
DRIVER=""

rm -rf out && mkdir -p out

junit_jar() {
    JUNIT="lib/junit-platform-console-standalone-${JUNIT_VERSION}.jar"
    need "org/junit/platform/junit-platform-console-standalone/${JUNIT_VERSION}/junit-platform-console-standalone-${JUNIT_VERSION}.jar" "$JUNIT"
    find src/main/java src/test/java -name "*.java" > out/sources.txt
    javac --release 21 -cp "$JUNIT" -d out @out/sources.txt
}

# number_in_report <label> <report-file>: the number on the summary line "[   48 tests <label>   ]"
number_in_report() {
    grep -E "tests $1 " "$2" | tr -d "\r" | grep -oE '[0-9]+' | head -1
}

if [ "${1:-}" = "--compile" ]; then
    junit_jar
    echo "OK: main and test code compile"
    exit 0
fi

if [ "${1:-}" = "--test" ]; then
    junit_jar
    java -jar "$JUNIT" execute --class-path out --scan-class-path --disable-banner --details=summary > out/report.txt 2>&1 \
        && { grep -E "tests (successful|failed)" out/report.txt | tr -d "\r"; echo "OK: all tests passed"; } \
        || { cat out/report.txt; echo "FAIL: see the report above"; exit 1; }
    exit 0
fi

if [ "${1:-}" = "--grade" ]; then
    junit_jar
    # Only the acceptance tests count here. Your own tests are marked by hand, using RUBRIC.md.
    java -jar "$JUNIT" execute --class-path out --scan-class-path --include-classname '.*AcceptanceTest.*' \
        --disable-banner --details=summary > out/grade.txt 2>&1 || true
    found="$(number_in_report found out/grade.txt)"
    passed="$(number_in_report successful out/grade.txt)"
    if [ -z "$found" ] || [ -z "$passed" ] || [ "$found" -eq 0 ]; then
        cat out/grade.txt
        echo "No acceptance test results were found. See the report above."
        exit 1
    fi
    points="$(awk -v p="$passed" -v f="$found" 'BEGIN { printf "%.1f", 40 * p / f }')"
    echo "Acceptance tests passed: $passed of $found"
    echo "Automatic points: $points of 40"
    [ "$passed" -eq "$found" ]
    exit $?
fi

find src/main/java -name "*.java" > out/sources.txt
javac --release 21 -d out @out/sources.txt

if [ "${1:-}" = "--check" ]; then
    java -cp out com.codechanakya.library.Main | tr -d "\r" | diff - expected-output.txt \
        && echo "OK: output matches expected-output.txt"
else
    java -cp out com.codechanakya.library.Main
fi
