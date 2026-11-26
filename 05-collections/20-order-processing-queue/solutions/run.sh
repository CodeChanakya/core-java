#!/usr/bin/env bash
# Compiles and runs the solution version of this program.
set -euo pipefail
cd "$(dirname "$0")"

rm -rf out && mkdir -p out
find src -name "*.java" > out/sources.txt
javac --release 21 -d out @out/sources.txt
if [ -d src/main/resources ]; then cp -r src/main/resources/. out/; fi
java -cp out com.codechanakya.queue.Main
