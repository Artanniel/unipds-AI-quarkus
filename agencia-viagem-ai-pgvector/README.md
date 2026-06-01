# 🌍 Agência de Viagens AI — RAG com pgvector

> **Agente de viagens inteligente** construído com Quarkus + LangChain4j + Ollama + PostgreSQL/pgvector.  
> Responde perguntas sobre pacotes de viagem utilizando **Retrieval-Augmented Generation (RAG)** com banco de dados vetorial.

---

## 🏗️ Arquitetura

```
┌──────────────┐     POST /travel     ┌──────────────────────┐
│    Cliente   │ ───────────────────► │  TravelAgentResource │
└──────────────┘                      └──────────┬───────────┘
                                                 │
                                    ┌────────────▼─────────────┐
                                    │      PackageExpert       │
                                    │   (AI Service / RAG)     │
                                    └──────┬──────────┬────────┘
                                           │          │
                              ┌────────────▼──┐  ┌───▼─────────────────┐
                              │    Ollama     │  │  PostgreSQL/pgvector │
                              │ (LLM + Embed) │  │  (Embedding Store)   │
                              └───────────────┘  └──────────────────────┘
```

### Fluxo de dados

1. **Ingestão (startup):** O `DocumentIngestor` lê `pacotes-viagem.md`, divide o texto em chunks, gera embeddings via `nomic-embed-text` e armazena no pgvector.
2. **Consulta (request):** O usuário envia uma pergunta → o `RagConfiguration` busca os chunks mais relevantes no pgvector → o contexto é injetado no prompt do LLM → resposta gerada pelo Ollama.

---

## 🧩 Stack Tecnológica

| Componente | Tecnologia |
|---|---|
| Framework | [Quarkus](https://quarkus.io/) 3.36+ |
| IA / LLM | [LangChain4j](https://quarkiverse.github.io/quarkiverse-docs/quarkus-langchain4j/dev/index.html) + Ollama |
| Modelo de Chat | `gpt-oss:20b` (configurável) |
| Modelo de Embedding | `nomic-embed-text` (768 dims) |
| Banco Vetorial | PostgreSQL + [pgvector](https://github.com/pgvector/pgvector) |
| Container Runtime | Podman / Docker |

---

## ⚙️ Pré-requisitos

- Java 17+
- Maven 3.9+
- [Podman](https://podman.io/) ou Docker
- [Ollama](https://ollama.com/) rodando localmente

---

## 🚀 Configuração e Execução

### 1. Iniciar os serviços de infraestrutura

```bash
# Subir Ollama + Open WebUI + PostgreSQL
podman-compose -f src/main/podman/podman-compose.yml up -d
```

### 2. Baixar os modelos necessários no Ollama

```bash
# Modelo de chat (escolha um)
podman exec ollama ollama pull gpt-oss:20b
# ou
podman exec ollama ollama pull gemma2:2b

# Modelo de embedding (obrigatório para o RAG)
podman exec ollama ollama pull nomic-embed-text
```

### 3. Executar a aplicação em modo dev

```bash
./mvnw quarkus:dev
```

> 💡 O Dev UI do Quarkus estará disponível em: http://localhost:8080/q/dev/

---

## 📁 Estrutura do Projeto

```
src/
├── main/
│   ├── java/dev/ia/ragWithPgVector/
│   │   ├── DocumentIngestor.java   # Carrega e vetoriza documentos no startup
│   │   ├── PackageExpert.java      # AI Service com System Prompt especializado
│   │   ├── RagConfiguration.java   # Configura o retriever do pgvector
│   │   ├── TravelAgentAssistant.java  # Interface do assistente genérico
│   │   └── TravelAgentResource.java   # REST endpoint POST /travel
│   └── resources/
│       ├── application.properties
│       └── rag/
│           └── pacotes-viagem.md   # Base de conhecimento dos pacotes
└── main/podman/
    └── podman-compose.yml          # Ollama + Open WebUI + PostgreSQL
```

---

## 🌐 Endpoint REST

### `POST /travel`

Envia uma pergunta para o agente de viagens.

```bash
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
# LLM - Ollama
quarkus.langchain4j.ollama.base-url=http://localhost:11434/
quarkus.langchain4j.ollama.chat-model.model-id=gpt-oss:20b
quarkus.langchain4j.ollama.embedding-model.model-id=nomic-embed-text
quarkus.langchain4j.ollama.timeout=360s

# pgvector
quarkus.langchain4j.pgvector.dimension=768
quarkus.langchain4j.pgvector.table=travel_embeddings
quarkus.langchain4j.pgvector.drop-table-first=true
```

> **Modelos alternativos disponíveis:** `gemma2:2b`, `llama3:8b`, `qwen2.5:7b`, `gemma4:31b-cloud`

---

## 📦 Build e Packaging

```bash
# JAR padrão
./mvnw package
java -jar target/quarkus-app/quarkus-run.jar

# Über-JAR (tudo em um arquivo)
./mvnw package -Dquarkus.package.type=uber-jar
java -jar target/*-runner.jar

# Executável nativo (requer GraalVM)
./mvnw package -Dnative
./target/agencia-viagem-ia-pgvector-1.0.0-SNAPSHOT-runner

# Executável nativo via container (sem GraalVM local)
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

---

## 🔗 Recursos e Referências

- [Quarkus LangChain4j — Guia Ollama](https://docs.quarkiverse.io/quarkus-langchain4j/dev/guide-ollama.html)
- [Quarkus LangChain4j — pgvector Embedding Store](https://docs.quarkiverse.io/quarkus-langchain4j/dev/index.html)
- [Quarkus — JDBC PostgreSQL](https://quarkus.io/guides/datasource)
- [Quarkus — REST](https://quarkus.io/guides/rest)
- [Projeto de referência — enterprise-ai-java-langchain4j](https://github.com/eldermoraes/enterprise-ai-java-langchain4j/tree/main/lesson-06/vector-database/travel-agency-ai)

---

## 🏭 Como criar um projeto Quarkus do zero com essas extensões

```bash
mvn io.quarkus.platform:quarkus-maven-plugin:3.2.10.Final:create \
  -DprojectGroupId=dev.ia \
  -DprojectArtifactId=agencia-viagem-ia-pgvector \
  -Dextensions="quarkus-rest,quarkus-langchain4j-ollama,quarkus-langchain4j-pgvector,quarkus-jdbc-postgresql"
```

> ⚠️ Execute esse comando a partir do **diretório pai** do projeto (não de dentro da pasta do projeto).