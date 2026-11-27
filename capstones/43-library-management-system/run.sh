#!/usr/bin/env bash
# Runs the REFERENCE solution, so the repository's checks can prove that the project is sound.
#   ./run.sh --check     the reference solution's demonstration against expected-output.txt
#   ./run.sh --grade     the acceptance tests against the reference solution: it must score 40 of 40
#
# To work on the exercise itself, use the starter folder:  cd starter && ./run.sh --grade
set -euo pipefail
exec bash "$(dirname "$0")/reference/run.sh" "$@"
