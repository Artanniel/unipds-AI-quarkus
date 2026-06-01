# 🌍 Agência de Viagens AI — RAG com Easy RAG

> **Agente de viagens inteligente** construído com Quarkus + LangChain4j + Ollama.  
> Responde perguntas sobre pacotes de viagem utilizando **Retrieval-Augmented Generation (RAG)** com a extensão Easy RAG do Quarkus LangChain4j.

---

## 🏗️ Arquitetura

```
┌──────────────┐     POST /travel     ┌───────────────────────┐
│    Cliente   │ ───────────────────► │  TravelAgentResource  │
└──────────────┘                      └───────────┬───────────┘
                                                  │
                                     ┌────────────▼─────────────┐
                                     │   TravelAgentAssistant   │
                                     │   (AI Service / RAG)     │
                                     └──────────┬───────────────┘
                                                │
                              ┌─────────────────▼──────────────────┐
                              │             Ollama                 │
                              │   (LLM + Easy RAG automático)      │
                              └────────────────────────────────────┘
                                              ▲
                              ┌───────────────┴──────────────────┐
                              │       src/main/resources/rag/    │
                              │       pacotes-viagem.md          │
                              └──────────────────────────────────┘
```

### Como o Easy RAG funciona

O **Easy RAG** do Quarkus LangChain4j carrega e indexa automaticamente todos os documentos do diretório configurado em `quarkus.langchain4j.easy-rag.path` na inicialização da aplicação — sem necessidade de banco de dados externo ou configuração manual de embeddings.

---

## 🧩 Stack Tecnológica

| Componente | Tecnologia |
|---|---|
| Framework | [Quarkus](https://quarkus.io/) |
| IA / LLM | [LangChain4j](https://docs.quarkiverse.io/quarkus-langchain4j/dev/guide-ollama.html) + Ollama |
| RAG | Easy RAG (in-memory, zero config) |
| Modelo de Chat | `gpt-oss:20b` (configurável) |
| Frontend | [Open WebUI](https://github.com/open-webui/open-webui) |
| Container Runtime | Podman |

---

## ⚙️ Pré-requisitos

- Java 17+
- Maven 3.9+
- [Podman](https://podman.io/) + podman-compose

---

## 🚀 Configuração e Execução

### 1. Iniciar os serviços de infraestrutura (Ollama + Open WebUI)

```shell script
podman-compose -f src/main/podman/podman-compose.yml up -d
```

Para parar os serviços:

```shell script
podman-compose -f src/main/podman/podman-compose.yml down
```

### 2. Baixar um modelo no Ollama

```shell script
# Pesquise modelos disponíveis em: https://ollama.com/search

# Baixar o modelo padrão
podman exec -it ollama ollama pull gpt-oss:20b

# Listar modelos instalados
podman exec -it ollama ollama list
```

### 3. Executar a aplicação em modo dev

```shell script
./mvnw quarkus:dev
```

ou

```shell script
mvn quarkus:dev
```

> 💡 O Dev UI do Quarkus estará disponível em: http://localhost:8080/q/dev/

---

## 📁 Estrutura do Projeto

```
src/
├── main/
│   ├── java/dev/ia/
│   │   ├── GreetingResource.java              # Endpoint de health check
│   │   ├── ragWithEasyRag/
│   │   │   ├── TravelAgentAssistant.java      # Interface do AI Service
│   │   │   └── TravelAgentResource.java       # REST endpoint POST /travel
│   │   └── ragWithPgVector/
│   │       └── DocumentIngestor.java          # (experimento) Ingestão manual
│   ├── podman/
│   │   └── podman-compose.yml                 # Ollama + Open WebUI
│   └── resources/
│       ├── application.properties
│       └── rag/
│           └── pacotes-viagem.md              # Base de conhecimento dos pacotes
```

---

## 🌐 Endpoint REST

### `POST /travel`

Envia uma pergunta para o agente de viagens.

```shell script
curl -X POST http://localhost:8080/travel \
  -H "Content-Type: text/plain" \
  -d "Quais são os pacotes disponíveis e seus preços?"
```

**Exemplo de resposta:**
```
Temos dois pacotes disponíveis:

🌿 Pacote Aventura Amazônia — R$ 4.500,00/pessoa
  7 dias | Focagem de jacarés, selva, comunidades ribeirinhas

🏛️ Pacote Tesouros do Egito — R$ 12.800,00/pessoa
  10 dias | Pirâmides de Gizé, cruzeiro no Nilo, Museu do Cairo
```

---

## 🗂️ Pacotes de Viagem Disponíveis

| Pacote | Duração | Preço | Cancelamento |
|---|---|---|---|
| 🌿 Aventura Amazônia | 7d / 6n | R$ 4.500/pessoa | 80% com 30 dias |
| 🏛️ Tesouros do Egito | 10d / 9n | R$ 12.800/pessoa | 50% com 30 dias |

---

## ⚙️ Configuração (`application.properties`)

```properties
# LLM — Ollama
quarkus.langchain4j.ollama.base-url=http://localhost:11434/
quarkus.langchain4j.ollama.chat-model.model-id=gpt-oss:20b
quarkus.langchain4j.ollama.timeout=360s

# Easy RAG — diretório com os documentos de conhecimento
quarkus.langchain4j.easy-rag.path=src/main/resources/rag
```

> **Modelos alternativos disponíveis:** `gemma2:2b`, `llama3:8b`, `qwen2.5:7b`, `gemma4:31b-cloud`, `gpt-oss:20b-cloud`

---

## 🐳 Subir o Ollama manualmente (sem Compose)

```shell script
podman run -d \
  -v ollama_data:/root/.ollama \
  -p 11434:11434 \
  --name ollama \
  --restart unless-stopped \
  ollama/ollama
```

## 🖥️ Subir o Open WebUI manualmente (sem Compose)

```shell script
podman run -d \
  -v open_webui_data:/app/backend/data \
  -p 3000:8080 \
  --name open-webui \
  --restart unless-stopped \
  ghcr.io/open-webui/open-webui:main
```

## Acessar o Open WebUI

1. Acesse http://localhost:3000
2. Selecione o modelo `gpt-oss:20b`
3. Clique em `Save`

Open WebUI com gpt-oss:20b local:
![Open WebUI — modelo local](Screenshot%20from%202026-05-31%2012-44-39.png)

![Open WebUI — chat](Screenshot%20from%202026-05-31%2014-26-59.png)

![Open WebUI — resultado](Screenshot%20from%202026-05-31%2014-28-07.png)

---

## 📦 Build e Packaging

```shell script
# JAR padrão
./mvnw package
java -jar target/quarkus-app/quarkus-run.jar

# Über-JAR (tudo em um arquivo)
./mvnw package -Dquarkus.package.type=uber-jar
java -jar target/*-runner.jar

# Executável nativo (requer GraalVM)
./mvnw package -Dnative
./target/agencia-viagem-ai-1.0.0-SNAPSHOT-runner

# Executável nativo via container (sem GraalVM local)
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

> Consulte https://quarkus.io/guides/maven-tooling para mais detalhes sobre builds nativos.

---

## 📚 Guias Relacionados

- [LangChain4j Ollama](https://docs.quarkiverse.io/quarkus-langchain4j/dev/guide-ollama.html) — Integração básica do Ollama com LangChain4j
- [REST](https://quarkus.io/guides/rest) — Web Services RESTful com Jakarta REST
- [REST (reactive)](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources) — Web Services reativos

---

## 🏭 Como criar o projeto do zero

```shell script
mvn io.quarkus.platform:quarkus-maven-plugin:3.2.10.Final:create \
  -DprojectGroupId=dev.ia \
  -DprojectArtifactId=agencia-viagem-ai \
  -Dextensions="quarkus-rest,quarkus-langchain4j-ollama"
```

> ⚠️ Execute a partir do **diretório pai** do projeto.

---

## 🔗 Referências

- [enterprise-ai-java-langchain4j — TravelAgentAssistant](https://github.com/eldermoraes/enterprise-ai-java-langchain4j/blob/main/lesson-03/rag/travel-agency-ai/src/main/java/dev/ia/TravelAgentAssistant.java)
- [quarkus-langchain4j docs](https://github.com/quarkiverse/quarkus-langchain4j/tree/main/docs/documentation/src/main/asciidoc/modules)