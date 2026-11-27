#!/usr/bin/env bash
# Compiles and runs this program.
#   ./run.sh             run it, using plain javac (no Maven needed)
#   ./run.sh --check     run it and compare the output with expected-output.txt
#   ./run.sh --maven     build and run it with Maven instead (needs `mvn` installed)
#
# The javac route does by hand what Maven does for you: compile src/main/java,
# then copy src/main/resources next to the classes.
set -euo pipefail
cd "$(dirname "$0")"

if [ "${1:-}" = "--maven" ]; then
    mvn -q -B package
    java -jar target/quote-calculator-1.0.0.jar
    exit 0
fi

rm -rf out && mkdir -p out
find src/main/java -name "*.java" > out/sources.txt
javac --release 21 -d out @out/sources.txt
cp -r src/main/resources/. out/

if [ "${1:-}" = "--check" ]; then
    java -cp out com.codechanakya.quotes.Main | tr -d "\r" | diff - expected-output.txt \
        && echo "OK: output matches expected-output.txt"
else
    java -cp out com.codechanakya.quotes.Main
fi
