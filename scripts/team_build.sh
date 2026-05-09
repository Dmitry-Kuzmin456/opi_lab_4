#!/bin/bash
set -e

TMP_TEAM_DIR="$1"
DIST_DIR="$2"

BASE_REV=$(svn info ^/trunk --show-item revision)

FIRST_REV=$(svn log -q build.xml | awk '/^r/{rev=substr($1,2)} END{print rev}')

START_REV=$((BASE_REV - 3))
END_REV=$BASE_REV

if [ "$START_REV" -lt "$FIRST_REV" ]; then
    START_REV=$FIRST_REV
fi

if [ "$END_REV" -lt "$START_REV" ]; then
    echo "Not enough previous revisions to build."
    exit 0
fi

echo "Building team revisions from r$START_REV to r$END_REV..."

for REV in $(seq "$START_REV" "$END_REV"); do
    CURRENT_DST="$TMP_TEAM_DIR/r$REV"
    echo "--- Processing revision r$REV ---"
    
    svn export -q -r "$REV" . "$CURRENT_DST"
    
    cp build.xml build.properties manifest.base.mf "$CURRENT_DST/"
    
    ant -q -f "$CURRENT_DST/build.xml" \
        -Ddist.dir="$CURRENT_DST/dist" \
        -Dbuild.dir="$CURRENT_DST/build" \
        build
done

echo "Team build completed successfully."