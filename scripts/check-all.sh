#!/usr/bin/env bash
# Runs every program's `./run.sh --check` and reports which ones pass.
# Used by CI, and handy before you commit: ./scripts/check-all.sh
set -uo pipefail
cd "$(dirname "$0")/.."

passed=0
failed=0

for script in [0-9][0-9]-*/[0-9][0-9]-*/run.sh capstones/*/run.sh; do
    [ -f "$script" ] || continue
    dir="$(dirname "$script")"
    if (cd "$dir" && ./run.sh --check > /dev/null 2>&1); then
        echo "PASS  $dir"
        passed=$((passed + 1))
    else
        echo "FAIL  $dir"
        failed=$((failed + 1))
    fi
done

echo
echo "$passed passed, $failed failed"
[ "$failed" -eq 0 ]
