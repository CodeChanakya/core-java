#!/usr/bin/env bash
# Compiles and runs the solution version of this program.
set -euo pipefail
cd "$(dirname "$0")"

rm -rf out && mkdir -p out
find src/main/java -name "*.java" > out/sources.txt
javac --release 21 -d out @out/sources.txt
cp -r src/main/resources/. out/
java -cp out com.codechanakya.quotes.Main
