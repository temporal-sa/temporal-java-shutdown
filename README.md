# temporal-java-shutdown

## Run Temporal Server

```bash
temporal server start-dev
```

## Run Worker

```bash
./gradlew bootJar && java -jar build/libs/temporal-java-shutdown-1.0.0.jar
```

## Start Workflow

```bash
temporal workflow start --type ShutdownJava --task-queue shutdown-task-queue --input '{"val":"foo"}'
```

## Kill Worker

After the workflow is started, kill the worker with `ctrl+c`. Observe the graceful worker shutdown,
and confirm successful completion of the workflow in the Temporal Web UI.

## Chaos Testing

### Build

Build the application jar by running `./gradlew bootJar`

### Run Workers

Open two (or more) terminals and run `./scripts/flaky_worker.sh` in each terminal. The script will, in a
continuous loop, a) start a worker in a background process, b) wait 20 seconds, and then c) send a `SIGTERM`
signal to terminate the process. This will test the worker's ability to handle a graceful shutdown.

### Start Workflows

In a separate terminal, run `./scripts/start.sh` to start many workflows in parallel.

### Observe the Chaos

Observe the logs in the terminal windows to see the periodic shutdown and restart of the worker processes.
In the Temporal UI, you will see all Workflow Executions complete successfully, with no failures, *nor any
task timeouts*!

## Docker

### Build

```bash
docker build -t temporal-java-shutdown:1.0.0 .
```

### Run

To run against Temporal Cloud, set the environment
variables `${TEMPORAL_ADDRESS}`, `${TEMPORAL_NAMESPACE}`, `${TEMPORAL_TLS_CERT}`, `${TEMPORAL_TLS_KEY}`
and `${TEMPORAL_TLS_KEY_PKCS8}`.

```bash
docker run -it --rm \
  --mount type=bind,source=$TEMPORAL_TLS_KEY_PKCS8,target=/certs/tls.key,readonly \
  --mount type=bind,source=$TEMPORAL_TLS_CERT,target=/certs/tls.crt,readonly \
  -e SPRING_PROFILE=tc \
  -e TEMPORAL_ADDRESS \
  -e TEMPORAL_NAMESPACE \
  -e TEMPORAL_TLS_CERT=/certs/tls.crt \
  -e TEMPORAL_TLS_KEY_PKCS8=/certs/tls.key \
  temporal-java-shutdown:1.0.0
```

### Publish

```bash
docker buildx build --platform linux/amd64,linux/arm64 -t pvsone/temporal-java-shutdown:1.0.0 . --push
```

## Kubernetes

### Deploy

Create a secret for the TLS client certificate and key

```bash
kubectl create secret generic client-credential \
  --from-file=tls.key=${TEMPORAL_TLS_KEY_PKCS8} \
  --from-file=tls.crt=${TEMPORAL_TLS_CERT}
```

Deploy the Worker

```bash
envsubst < deploy.yaml | kubectl apply -f -
```

### Chaos Testing

Delete Pods at random to simulate chaos.

```bash
for i in {1..20}; do ./scripts/delete_random_pod.sh; sleep 1; done
```

In a separate terminal, run `./scripts/start.sh` to start many workflows in parallel.
