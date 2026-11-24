#!/usr/bin/env bash
# Compiles and runs the solution version of this program.
set -euo pipefail
cd "$(dirname "$0")"

rm -rf out && mkdir -p out
find src -name "*.java" > out/sources.txt
javac --release 21 -d out @out/sources.txt
java -cp out com.codechanakya.employees.Main
