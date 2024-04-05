#!/bin/bash

while true
do
  java -jar build/libs/simple-java-0.0.1-SNAPSHOT.jar &
  PID=$!
  sleep 20
  kill -s TERM $PID
done
