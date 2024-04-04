# temporal-java-shutdown

## Run Temporal Server

```bash
temporal server start-dev
```

## Run Worker

```bash
./gradlew bootJar && java -jar build/libs/simple-java-0.0.1-SNAPSHOT.jar

# after the workflow is started (using command below), kill the worker with ctrl+c
```

## Start Workflow

```bash
temporal workflow start --type SimpleJava --task-queue simple-task-queue --input '{"val":"foo"}'
```
