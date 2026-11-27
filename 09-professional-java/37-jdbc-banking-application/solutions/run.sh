#!/usr/bin/env bash
# Compiles and runs the solution version of this program.
# It uses the database driver jars that ../run.sh downloads into ../lib, so run that once first.
set -euo pipefail
cd "$(dirname "$0")"

DERBY_VERSION="10.16.1.1"
SEP=":"
case "$(uname -s)" in MINGW*|MSYS*|CYGWIN*) SEP=";" ;; esac
DRIVER="../lib/derby-${DERBY_VERSION}.jar${SEP}../lib/derbyshared-${DERBY_VERSION}.jar"
if [ ! -f "../lib/derby-${DERBY_VERSION}.jar" ]; then
    echo "The database driver is not downloaded yet. Run ../run.sh once, then try again."
    exit 1
fi

rm -rf out && mkdir -p out
find src/main/java -name "*.java" > out/sources.txt
javac --release 21 -d out @out/sources.txt
java -cp "out${SEP}${DRIVER}" com.codechanakya.jdbcbank.Main
