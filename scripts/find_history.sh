#!/bin/bash
set -e

DIST_DIR=$1     
START_REV=$(svn info --show-item revision)
WORK_REV=$START_REV
OK_REV=''

echo "Starting search from revision $START_REV..."

while [ -n "$WORK_REV" ] && [ "$WORK_REV" -ge 1 ]; do
    svn up -r $WORK_REV >/dev/null
    
    if ant -q compile; then
        OK_REV=$WORK_REV
        break
    fi
    
    if [ "$WORK_REV" -eq 1 ]; then break; fi
    WORK_REV=$((WORK_REV-1))
done

if [ -z "$OK_REV" ]; then
    svn up -r $START_REV >/dev/null
    echo "No compilable svn revision found"
    exit 1
fi

NEXT_REV=$((OK_REV+1))
mkdir -p "$DIST_DIR"

if [ "$NEXT_REV" -le "$START_REV" ]; then
    svn diff -c $NEXT_REV > "$DIST_DIR/history-last-working.diff"
else
    : > "$DIST_DIR/history-last-working.diff"
fi

svn up -r $START_REV >/dev/null
echo "Last compilable revision: $OK_REV"