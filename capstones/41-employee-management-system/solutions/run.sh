#!/usr/bin/env bash
# Copies the solution sources over a temporary copy of the project and runs its tests.
#   ./run.sh --test
set -euo pipefail
cd "$(dirname "$0")"
WORK="$(mktemp -d)"
trap 'rm -rf "$WORK"' EXIT
cp -r .. "$WORK/project"
rm -rf "$WORK/project/src" "$WORK/project/solutions" "$WORK/project/out"
cp -r src "$WORK/project/src"
bash "$WORK/project/run.sh" "${1:---test}"
