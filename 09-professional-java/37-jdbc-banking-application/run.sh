#!/usr/bin/env bash
# Compiles and runs this program.
#   ./run.sh             run it
#   ./run.sh --check     run it and compare the output with expected-output.txt
#   ./run.sh --maven     build and run it with Maven instead (needs `mvn` installed)
#
# The database driver is two jars. This script downloads them once into lib/ (which Git
# ignores). With --maven, the pom.xml lists the driver as a dependency and Maven fetches it.
set -euo pipefail
cd "$(dirname "$0")"

if [ "${1:-}" = "--maven" ]; then
    exec mvn -q -B compile exec:java
fi

DERBY_VERSION="10.16.1.1"
# BASE can be pointed at a local folder laid out like Maven Central (used for testing this script offline).
BASE="${MAVEN_BASE:-https://repo1.maven.org/maven2}"
MIRROR="https://maven-central.storage-download.googleapis.com/maven2"
JARS=("derby" "derbyshared")

download() {
    local artifact="$1"
    local path="org/apache/derby/${artifact}/${DERBY_VERSION}/${artifact}-${DERBY_VERSION}.jar"
    local target="lib/${artifact}-${DERBY_VERSION}.jar"
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

for artifact in "${JARS[@]}"; do
    if [ ! -f "lib/${artifact}-${DERBY_VERSION}.jar" ]; then
        echo "Downloading ${artifact} ${DERBY_VERSION} (one time only)..."
        download "$artifact" || { echo "Could not download ${artifact}. Check your internet connection and try again in a minute."; exit 1; }
    fi
done

# The class-path separator is ":" on Linux and macOS, and ";" in Git Bash on Windows.
SEP=":"
case "$(uname -s)" in MINGW*|MSYS*|CYGWIN*) SEP=";" ;; esac
CP="out${SEP}lib/derby-${DERBY_VERSION}.jar${SEP}lib/derbyshared-${DERBY_VERSION}.jar"

rm -rf out && mkdir -p out
find src/main/java -name "*.java" > out/sources.txt
javac --release 21 -d out @out/sources.txt

# Nothing in this program touches the driver at compile time, so only `java` needs it.
if [ "${1:-}" = "--check" ]; then
    java -cp "$CP" com.codechanakya.jdbcbank.Main | tr -d "\r" | diff - expected-output.txt \
        && echo "OK: output matches expected-output.txt"
else
    java -cp "$CP" com.codechanakya.jdbcbank.Main
fi
