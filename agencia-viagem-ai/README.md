# agencia-viagem-ai

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

You can run your application in dev mode that enables live coding using:
```shell script
mvn quarkus:dev
```


> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/agencia-viagem-ai-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Related Guides

- LangChain4j Ollama ([guide](https://docs.quarkiverse.io/quarkus-langchain4j/dev/guide-ollama.html)): Provides the basic integration of Ollama with LangChain4j
- REST ([guide](https://quarkus.io/guides/rest)): Build RESTful web services and APIs using Jakarta REST (formerly JAX-RS)

## Provided Code

### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)

## Podman Compose

To run the application in dev mode with Podman Compose, use the following command:
```shell script
podman compose -f src/main/podman/podman-compose.yml up -d
```

To stop the application, use the following command:
```shell script
podman compose -f src/main/podman/podman-compose.yml down
```

## Ollama Server

```shell script
podman run -d \
  -v ollama_data:/root/.ollama \
  -p 11434:11434 \
  --name ollama \
  --restart unless-stopped \
  ollama/ollama
```
## List models in Ollama server

```shell script
podman exec -it ollama ollama list
```

## Search model in Ollama registry

https://ollama.com/search

## Download Model

```shell script
podman exec -it ollama ollama pull gpt-oss:20b
```

## Create Admin User

```shell script
podman exec -it open-webui ollama create admin
```

## Open WebUI

```shell script
podman run -d \
  -v open_webui_data:/app/backend/data \
  -p 3000:8080 \
  --name open-webui \
  --restart unless-stopped \
  ghcr.io/open-webui/open-webui:main
```

## Select model in Open WebUI

1. Go to http://localhost:3000
2. Select the model `gpt-oss:20b`
3. Click on `Save`

Open WebUI with gpt-oss:20b local:
![alt text](<Screenshot from 2026-05-31 12-44-39.png>)

![alt text](<Screenshot from 2026-05-31 14-26-59.png>)

![alt text](<Screenshot from 2026-05-31 14-28-07.png>)


## Reference project

[- https://github.com/quarkiverse/quarkus-langchain4j/tree/main/docs/documentation/src/main/asciidoc/modules](https://github.com/eldermoraes/enterprise-ai-java-langchain4j/blob/main/lesson-03/rag/travel-agency-ai/src/main/java/dev/ia/TravelAgentAssistant.java)