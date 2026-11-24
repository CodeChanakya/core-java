#!/usr/bin/env bash
# Compiles and runs this program.
#   ./run.sh           run it
#   ./run.sh --check   run it and compare the output with expected-output.txt
set -euo pipefail
cd "$(dirname "$0")"

rm -rf out && mkdir -p out
find src/main/java -name "*.java" > out/sources.txt
javac --release 21 -d out @out/sources.txt

if [ "${1:-}" = "--check" ]; then
    java -cp out com.codechanakya.payments.Main | tr -d "\r" | diff - expected-output.txt \
        && echo "OK: output matches expected-output.txt"
else
    java -cp out com.codechanakya.payments.Main
fi
