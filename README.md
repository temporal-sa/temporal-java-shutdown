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

Open two (or more) terminals and run `./worker.sh` in each terminal. The worker script will, in a continuous
loop, a) start a worker in a background process, b) wait 20 seconds, and then c) send a `SIGTERM` signal to 
terminate the process. This will test the worker's ability to handle a graceful shutdown.

### Start Workflows

In a separate terminal, run `./start.sh` to start many workflows in parallel.

### Observe the Chaos

Observe the logs in the terminal windows to see the periodic shutdown and restart of the worker processes.
In the Temporal UI, you will see all Workflow Executions complete successfully, with no failures, *nor any
task timeouts*!
