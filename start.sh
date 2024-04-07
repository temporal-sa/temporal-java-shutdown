#!/bin/bash

for i in {1..200}
do
  temporal workflow start --type ShutdownJava --task-queue shutdown-task-queue --input '{"val":"foo"}'
  sleep 1
done
