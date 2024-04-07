#!/bin/bash

PID=0

cleanup() {
  echo " **** Ctrl+C detected. Cleaning up..."
  if [ $PID -ne 0 ]; then
    kill -s TERM $PID
  fi
  exit 0
}

trap 'cleanup' SIGINT

while true
do
  java -jar build/libs/temporal-java-shutdown-1.0.0.jar &
  PID=$!
  sleep 20
  kill -s TERM $PID
done
