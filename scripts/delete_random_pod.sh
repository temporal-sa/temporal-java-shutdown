#!/bin/bash

PODS=$(kubectl get pods -o jsonpath='{.items[*].metadata.name}')
POD_ARRAY=($PODS)
NUM_PODS=${#POD_ARRAY[@]}

if [ $NUM_PODS -eq 0 ]; then
  echo "No Pods found in the $NAMESPACE namespace."
  exit 1
fi

RANDOM_INDEX=$(($RANDOM % $NUM_PODS))
POD_TO_DELETE=${POD_ARRAY[$RANDOM_INDEX]}

echo "Deleting Pod $POD_TO_DELETE ..."
kubectl delete pod "$POD_TO_DELETE"
echo "Pod $POD_TO_DELETE has been deleted."
