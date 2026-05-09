#!/bin/bash
set -e

FORBIDDEN_FILES=$1

# Sync working copy with repository before diff/commit checks.
svn update

CHANGED=$(svn status | awk '/^[AMR]/{print $2}')

if [ -z "$CHANGED" ]; then
    echo "No SVN changes to check."
    exit 0
fi

FORBIDDEN=()
TO_COMMIT=()
for F in $CHANGED; do
    BASE=$(basename "$F")
    
    case ",$FORBIDDEN_FILES," in
        *,"$BASE",*)
            FORBIDDEN+=("$F")
            ;;
        *)
            TO_COMMIT+=("$F")
            ;;
    esac
done

if [ ${#FORBIDDEN[@]} -gt 0 ]; then
    echo "-------------------------------------------------------"
    echo "Some files are not allowed for auto-commit and will be skipped:"
    printf ' %s\n' "${FORBIDDEN[@]}"
    echo "-------------------------------------------------------"
fi

if [ ${#TO_COMMIT[@]} -eq 0 ]; then
    echo "No files eligible for auto-commit."
    exit 0
fi

echo "Committing all non-forbidden files:"
printf ' %s\n' "${TO_COMMIT[@]}"
svn commit -m "Auto-commit from Ant diff target: updated non-forbidden files" "${TO_COMMIT[@]}"