#!/usr/bin/env bash
# Compiles and runs the solution version of this program.
#   ./run.sh          run the demonstration
#   ./run.sh --test   run the solution's JUnit tests (needs the jars ../run.sh --test downloads)
# It uses the jars that ../run.sh downloads into ../lib, so run that once first.
set -euo pipefail
cd "$(dirname "$0")"

DERBY_VERSION="10.16.1.1"
JUNIT="../lib/junit-platform-console-standalone-1.11.4.jar"
SEP=":"
case "$(uname -s)" in MINGW*|MSYS*|CYGWIN*) SEP=";" ;; esac
DRIVER="../lib/derby-${DERBY_VERSION}.jar${SEP}../lib/derbyshared-${DERBY_VERSION}.jar"
if [ ! -f "../lib/derby-${DERBY_VERSION}.jar" ]; then
    echo "The jars are not downloaded yet. Run ../run.sh --test once, then try again."
    exit 1
fi

rm -rf out && mkdir -p out
if [ "${1:-}" = "--test" ]; then
    [ -f "$JUNIT" ] || { echo "The JUnit jar is missing. Run ../run.sh --test once, then try again."; exit 1; }
    find src/main/java src/test/java -name "*.java" > out/sources.txt
    javac --release 21 -cp "$JUNIT" -d out @out/sources.txt
    java -jar "$JUNIT" execute --class-path "out${SEP}${DRIVER}" --scan-class-path --disable-banner --details=summary
else
    find src/main/java -name "*.java" > out/sources.txt
    javac --release 21 -d out @out/sources.txt
    java -cp "out${SEP}${DRIVER}" com.codechanakya.empdb.Main
fi
