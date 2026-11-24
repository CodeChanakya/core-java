#!/usr/bin/env bash
# Compiles and runs this program.
#   ./run.sh           run it
#   ./run.sh --check   run it and compare the output with expected-output.txt
set -euo pipefail
cd "$(dirname "$0")"

mkdir -p out
javac --release 21 -d out src/main/java/*.java

if [ "${1:-}" = "--check" ]; then
    java -cp out SalaryCalculator  | tr -d "\r" | diff - expected-output.txt \
        && echo "OK: output matches expected-output.txt"
else
    java -cp out SalaryCalculator
fi
