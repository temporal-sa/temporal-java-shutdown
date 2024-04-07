#!/bin/bash

while true
do
  java -jar build/libs/temporal-java-shutdown-1.0.0.jar &
  PID=$!
  sleep 20
  kill -s TERM $PID
done
