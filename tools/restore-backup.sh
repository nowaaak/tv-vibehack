#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
BACKUP="${1:-$(cat backup/LATEST)}"
[ -f "$BACKUP/packages_enabled.txt" ] || { echo "usage: restore-backup.sh <backup dir>"; exit 1; }
PY=tools/venv/bin/python
[ -x "$PY" ] || PY=tools/venv/Scripts/python.exe
export MSYS_NO_PATHCONV=1
STOCK_HOME="$(grep '/' "$BACKUP/home_activity.txt" | tail -n1 | tr -d '[:space:]')"
STOCK_IME="$(grep '^default_input_method=' "$BACKUP/settings_secure.txt" | cut -d= -f2- | tr -d '[:space:]')"
ENABLED_NOW="$("$PY" -I tools/tv.py 'pm list packages -e' | tr -d '\r' | sed 's/^package://' | sort)"
ENABLED_THEN="$(tr -d '\r' < "$BACKUP/packages_enabled.txt" | sed 's/^package://' | sort)"
{
  comm -13 <(echo "$ENABLED_NOW") <(echo "$ENABLED_THEN") | { grep -v '^$' || true; } \
    | awk '{print "cmd package install-existing " $1; print "pm enable " $1}'
  echo "cmd package set-home-activity --user 0 $STOCK_HOME"
  [ -n "$STOCK_IME" ] && echo "ime set $STOCK_IME"
  echo "pm list packages -d"
} | "$PY" -I tools/tv.py --batch
