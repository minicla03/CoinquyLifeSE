#!/bin/bash

# Directory di lavoro (dove si trovano i JAR)
WORKDIR="$(cd "$(dirname "$0")" && pwd)"
LOGDIR="$WORKDIR/logs"
PIDDIR="$WORKDIR/pids"

mkdir -p "$LOGDIR"
mkdir -p "$PIDDIR"

for jar in "$WORKDIR"/*.jar; do
    [ -e "$jar" ] || continue  # salta se non ci sono jar
    jar_name=$(basename "$jar")
    name="${jar_name%.jar}"

    echo "Avviando $jar_name..."
    nohup java -jar "$jar" > "$LOGDIR/$name.out" 2> "$LOGDIR/$name.err" < /dev/null &
    echo $! > "$PIDDIR/$name.pid"
    echo "$jar_name avviato con PID $(cat "$PIDDIR/$name.pid")"
done
