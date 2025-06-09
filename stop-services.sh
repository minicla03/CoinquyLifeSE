#!/bin/bash

# Directory in cui si trovano i file PID
WORKDIR="$(cd "$(dirname "$0")" && pwd)"
PIDDIR="$WORKDIR/pids"

if [ ! -d "$PIDDIR" ]; then
    echo "Nessuna directory pids/ trovata. Nulla da fermare."
    exit 1
fi

for pidfile in "$PIDDIR"/*.pid; do
    [ -f "$pidfile" ] || continue
    pid=$(cat "$pidfile")
    name=$(basename "$pidfile" .pid)

    if kill -0 "$pid" >/dev/null 2>&1; then
        echo "Terminando $name (PID $pid)..."
        kill "$pid"
    else
        echo "$name (PID $pid) non è in esecuzione."
    fi

    rm -f "$pidfile"
done
