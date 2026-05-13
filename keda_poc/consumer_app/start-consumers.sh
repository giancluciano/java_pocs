#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
APP_DIR="${SCRIPT_DIR}/consumer-app"

cd "${APP_DIR}"

echo "Building consumer-app..."
mvn -q package -DskipTests

PIDS=()

cleanup() {
  echo
  echo "Stopping consumers..."
  for pid in "${PIDS[@]}"; do
    if kill -0 "${pid}" 2>/dev/null; then
      kill "${pid}" 2>/dev/null || true
    fi
  done
  wait "${PIDS[@]}" 2>/dev/null || true
  echo "Done."
}
trap cleanup INT TERM EXIT

start_instance() {
  local id="$1"
  local prefix="[consumer-${id}]"
  mvn -q exec:java 2>&1 | sed -u "s|^|${prefix} |" &
  PIDS+=("$!")
}

echo "Starting 2 consumer instances (same group: consumer-app-group)..."
start_instance 1
start_instance 2

wait
