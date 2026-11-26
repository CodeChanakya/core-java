#!/usr/bin/env bash
# Compiles and runs the solution version of this program.
# It runs from the program folder (one level up), because the program reads data/ from there.
set -euo pipefail
cd "$(dirname "$0")/.."

rm -rf solutions/out && mkdir -p solutions/out
find solutions/src -name "*.java" > solutions/out/sources.txt
javac --release 21 -d solutions/out @solutions/out/sources.txt
java -cp solutions/out com.codechanakya.files.Main
