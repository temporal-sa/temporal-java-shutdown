#!/bin/bash

for i in {1..100}
do
  temporal workflow start --type SimpleJava --task-queue simple-task-queue --input '{"val":"foo"}'
  sleep 1
done
