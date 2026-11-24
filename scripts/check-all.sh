#!/usr/bin/env bash
# Runs every program's `run.sh --check` and reports which ones pass.
# Used by CI, and handy before you commit: ./scripts/check-all.sh
#
# The scripts are started with `bash run.sh`, not `./run.sh`, so a missing
# executable bit (common when files are added from Windows) cannot break the run.
# When a program fails, the last lines of its output are printed to say why.
set -uo pipefail
cd "$(dirname "$0")/.."

passed=0
failed=0

for script in [0-9][0-9]-*/[0-9][0-9]-*/run.sh capstones/*/run.sh; do
    [ -f "$script" ] || continue
    dir="$(dirname "$script")"
    if out=$(cd "$dir" && bash run.sh --check 2>&1); then
        echo "PASS  $dir"
        passed=$((passed + 1))
    else
        echo "FAIL  $dir"
        echo "$out" | tail -8 | sed 's/^/        /'
        failed=$((failed + 1))
    fi
done

echo
echo "$passed passed, $failed failed"
[ "$failed" -eq 0 ]
