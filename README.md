# 🤖 unipds-AI-quarkus

> Repositório de estudos e experimentações com **Inteligência Artificial em Java**, utilizando [Quarkus](https://quarkus.io/) como framework base e [LangChain4j](https://docs.langchain4j.dev/) como camada de integração com modelos de linguagem.

Cada sub-projeto explora uma abordagem diferente de construção de agentes e assistentes inteligentes — da forma mais simples (Easy RAG) até arquiteturas mais sofisticadas com banco de dados vetorial e agentes autônomos com ferramentas.

---

## 🗺️ Índice de Projetos

| Projeto | Abordagem RAG | Banco Vetorial | Agentes | Sessão / Auth | Complexidade |
|---|---|---|---|---|---|
| [agencia-viagem-ai](#-agencia-viagem-ai) | Easy RAG (in-memory) | ❌ | ❌ | ❌ | ⭐ Iniciante |
| [agencia-viagem-ai-pgvector](#-agencia-viagem-ai-pgvector) | RAG manual + pgvector | ✅ PostgreSQL | ❌ | ❌ | ⭐⭐ Intermediário |
| [agencia-viagem-ai-agents](#-agencia-viagem-ai-agents) | RAG + pgvector | ✅ PostgreSQL | ✅ Tools | ❌ | ⭐⭐⭐ Avançado |
| [agencia-viagem-ai-agents-with-session](#-agencia-viagem-ai-agents-with-session) | RAG + pgvector | ✅ PostgreSQL | ✅ Tools + Category | ✅ ThreadLocal | ⭐⭐⭐⭐ Expert |
| [mcp-booking-server](#-mcp-booking-server) | ❌ | ❌ | Servidor MCP | ❌ | ⭐ Intermediário |
| [mcp-booking-server-with-guardrails](#-mcp-booking-server-with-guardrails) | ❌ | ❌ | Servidor MCP | ❌ | ⭐⭐ Intermediário |
| [travel-agency-ai-mcp](#-travel-agency-ai-mcp) | RAG + pgvector | ✅ PostgreSQL | ✅ Cliente MCP | ❌ | ⭐⭐⭐ Avançado |
| [travel-agency-ai-mcp-guardrails](#-travel-agency-ai-mcp-guardrails) | RAG + pgvector | ✅ PostgreSQL | ✅ Cliente MCP + Guardrails | ❌ | ⭐⭐⭐⭐ Expert |

---

## 🧩 Stack Compartilhada

Todos os projetos compartilham a mesma base tecnológica:

| Componente | Tecnologia |
|---|---|
| Framework | [Quarkus](https://quarkus.io/) 3.36+ |
| Integração IA | [LangChain4j](https://docs.langchain4j.dev/) via Quarkus Extension |
| LLM | [Ollama](https://ollama.com/) (local) — `gpt-oss:20b`, `gemma2:2b`, etc. |
| Build | Maven Wrapper (`./mvnw`) |
| Container Runtime | [Podman](https://podman.io/) + podman-compose |
| Java | 17+ |

---

## 📦 agencia-viagem-ai

> **Caminho:** `./agencia-viagem-ai`

### O que é?

O projeto de entrada da série. Demonstra como construir um agente de viagens com **RAG zero-configuration** usando o **Easy RAG** do Quarkus LangChain4j.

O Easy RAG carrega e indexa automaticamente todos os documentos de um diretório na inicialização — sem banco de dados externo, sem pipelines de embedding manuais.

### Fluxo

```
Cliente  →  POST /travel  →  TravelAgentAssistant (RAG)  →  Ollama  →  resposta
                                        ↑
                             src/main/resources/rag/
                             pacotes-viagem.md (in-memory)
```

### Destaques

- ✅ **Zero infraestrutura** — apenas Ollama rodando localmente
- ✅ **Easy RAG** — indexação automática de documentos Markdown
- ✅ **Open WebUI** integrado via podman-compose para interface visual

### Extensões Quarkus

```
quarkus-rest
quarkus-langchain4j-ollama
quarkus-langchain4j-easy-rag
```

### Como rodar

```bash
cd agencia-viagem-ai

# Subir Ollama + Open WebUI
podman-compose -f src/main/podman/podman-compose.yml up -d

# Executar a aplicação
./mvnw quarkus:dev
```

📖 [README completo do projeto →](./agencia-viagem-ai/README.md)

---

## 📦 agencia-viagem-ai-pgvector

> **Caminho:** `./agencia-viagem-ai-pgvector`

### O que é?

Evolução do projeto anterior. Substitui o índice in-memory do Easy RAG por um **banco de dados vetorial real** — PostgreSQL com a extensão [pgvector](https://github.com/pgvector/pgvector).

Aqui o pipeline de RAG é totalmente explícito: o `DocumentIngestor` fragmenta os documentos, gera embeddings com `nomic-embed-text` e armazena vetores no PostgreSQL. A busca semântica é feita pelo `RagConfiguration` em cada requisição.

### Fluxo

```
[Startup]  DocumentIngestor  →  chunking  →  nomic-embed-text  →  pgvector (PostgreSQL)

[Request]  Cliente  →  POST /travel  →  PackageExpert
                                              ↓
                              RagConfiguration (busca vetorial)
                                              ↓
                                    pgvector + Ollama LLM  →  resposta
```

### Destaques

- ✅ **Pipeline RAG explícito** — controle total sobre ingestão e recuperação
- ✅ **PostgreSQL + pgvector** — embeddings persistentes entre reinicializações
- ✅ **Modelo de embedding dedicado** — `nomic-embed-text` (768 dimensões)
- ✅ **System Prompt especializado** via `@SystemMessage` no `PackageExpert`

### Extensões Quarkus

```
quarkus-rest
quarkus-langchain4j-ollama
quarkus-langchain4j-pgvector
quarkus-jdbc-postgresql
```

### Como rodar

```bash
cd agencia-viagem-ai-pgvector

# Subir Ollama + PostgreSQL/pgvector + Open WebUI
podman-compose -f src/main/podman/podman-compose.yml up -d

# Baixar o modelo de embedding (obrigatório)
podman exec ollama ollama pull nomic-embed-text

# Executar a aplicação
./mvnw quarkus:dev
```

📖 [README completo do projeto →](./agencia-viagem-ai-pgvector/README.md)

---

## 📦 agencia-viagem-ai-agents

> **Caminho:** `./agencia-viagem-ai-agents`

### O que é?

O projeto mais avançado da série. Além do RAG com pgvector, introduz o conceito de **AI Agents** — agentes que possuem ferramentas (`@Tool`) e podem executar ações de forma autônoma, como consultar disponibilidade, realizar reservas e verificar status.

O `PackageExpert` passa a ser um agente que raciocina sobre qual ferramenta chamar (via `BookingTools`) antes de responder ao usuário, seguindo o padrão **ReAct** (Reasoning + Acting).

### Fluxo

```
Cliente  →  POST /travel  →  PackageExpert (AI Agent)
                                      ↓
                          ┌───────────┴──────────────┐
                          │                          │
                   pgvector RAG              BookingTools (@Tool)
                (busca semântica)        ┌────────────────────────┐
                          │             │ • checkAvailability()   │
                          │             │ • bookPackage()         │
                          │             │ • getBookingStatus()    │
                          └──────┬──────┘ • cancelBooking()      │
                                 │        └────────────────────────┘
                               Ollama LLM
                                 │
                               resposta
```

### Destaques

- ✅ **AI Agents com ferramentas** — o LLM decide quando e como chamar cada `@Tool`
- ✅ **Memória de conversa** — chat memory configurável (`MessageWindowChatMemory`)
- ✅ **RAG + Agentes combinados** — recuperação semântica + ações autônomas
- ✅ **Ciclo completo de reservas** — disponibilidade, reserva, status e cancelamento

### Extensões Quarkus

```
quarkus-rest
quarkus-langchain4j-ollama
quarkus-langchain4j-pgvector
quarkus-jdbc-postgresql
```

### Como rodar

```bash
cd agencia-viagem-ai-agents

# Subir Ollama + PostgreSQL/pgvector
podman-compose -f src/main/podman/podman-compose.yml up -d

# Baixar os modelos
podman exec ollama ollama pull gpt-oss:20b
podman exec ollama ollama pull nomic-embed-text

# Executar a aplicação
./mvnw quarkus:dev
```

📖 [README completo do projeto →](./agencia-viagem-ai-agents/README.md)

---

## 📦 agencia-viagem-ai-agents-with-session

> **Caminho:** `./agencia-viagem-ai-agents-with-session`

### O que é?

O projeto mais completo da série. Expande o projeto de agentes adicionando **identidade de sessão por usuário** — cada chamada à API identifica o usuário via header HTTP `X-User-Name`, e essa identidade é propagada por `ThreadLocal` para controlar autorizações dentro dos `@Tool`s.

Introduz também o conceito de **categorização de pacotes** com um enum `Category` (`ADVENTURE`, `TREASURES`), permitindo filtrar reservas por tipo. O cancelamento de reservas agora valida se o usuário autenticado é realmente o dono da reserva — sem depender de dados informados pelo cliente.

### Fluxo

```
Cliente  →  POST /travel  +  Header: X-User-Name  →  TravelAgentResource
                                                              │
                                           SecurityContext.setCurrentUser(userName)
                                                              │
                                             PackageExpert.chat(memoryId=userName, ...)
                                                              │
                                   ┌──────────────────────────┼────────────────────────┐
                                   │                          │                        │
                            pgvector RAG              BookingTools                  Ollama
                         (busca semântica)     ┌──────────────────────────┐          LLM
                                   │          │ • getBookingDetails()    │           │
                                   │          │ • cancelBooking()        │           │
                                   │          │   (valida SecurityCtx)   │           │
                                   │          │ • listPackagesByCategory()│           │
                                   └──────────┴──────────────────────────┴───────────┘
                                                              │
                                                          resposta
                                           SecurityContext.clear() [finally]
```

### O que há de novo neste projeto

| Novidade | Descrição |
|---|---|
| `SecurityContext` | `ThreadLocal<String>` que propaga a identidade do usuário entre camadas |
| `X-User-Name` header | Simula autenticação via header HTTP na API REST |
| `memoryId = userName` | Memória de chat isolada **por usuário** (sem mais `session-123` fixo) |
| `Category` enum | Classifica pacotes em `ADVENTURE` e `TREASURES` |
| `listPackagesByCategory()` | Novo `@Tool` para filtrar pacotes por categoria |
| `cancelBooking()` | Cancelamento agora valida o usuário real via `SecurityContext` |
| Novos pacotes | `Trilha Inca` adicionado ao `BookingService` |

### Destaques

- ✅ **Memória por sessão real** — cada usuário tem seu próprio histórico de conversa
- ✅ **Segurança no @Tool** — cancelamento verifica identidade via `ThreadLocal`, não dados informados pelo usuário
- ✅ **Filtro por categoria** — agente pode recomendar pacotes por tipo (`ADVENTURE` / `TREASURES`)
- ✅ **Pattern `try/finally`** — garante limpeza do `SecurityContext` mesmo em exceções

### Extensões Quarkus

```
quarkus-rest
quarkus-langchain4j-ollama
quarkus-langchain4j-pgvector
quarkus-jdbc-postgresql
```

### Como rodar

```bash
cd agencia-viagem-ai-agents-with-session

# Subir Ollama + PostgreSQL/pgvector
podman-compose -f src/main/podman/podman-compose.yml up -d

# Baixar os modelos
podman exec ollama ollama pull gpt-oss:20b
podman exec ollama ollama pull nomic-embed-text

# Executar a aplicação
./mvnw quarkus:dev
```

```bash
# Exemplo de chamada com sessão de usuário
curl -X POST http://localhost:8080/travel \
  -H "Content-Type: text/plain" \
  -H "X-User-Name: John Doe" \
  -d "Quero cancelar minha reserva 12345"
```

📖 [README completo do projeto →](./agencia-viagem-ai-agents-with-session/README.md)

---

## 📦 mcp-booking-server

> **Caminho:** `./mcp-booking-server`

### O que é?

Servidor MCP (Model Context Protocol) básico que expõe as capacidades de reservas (bookings) da agência de viagens. Utilizado para fornecer as ferramentas (tools) para os agentes inteligentes em outros projetos via protocolo MCP.

📖 [README completo do projeto →](./mcp-booking-server/README.md)

---

## 📦 mcp-booking-server-with-guardrails

> **Caminho:** `./mcp-booking-server-with-guardrails`

### O que é?

Servidor MCP avançado que, além de expor as ferramentas de reservas, conta com mecanismos de Server-Sent Events (SSE) e preparativos para trabalhar com clientes seguros usando guardrails.

📖 [README completo do projeto →](./mcp-booking-server-with-guardrails/README.md)

---

## 📦 travel-agency-ai-mcp

> **Caminho:** `./travel-agency-ai-mcp`

### O que é?

Agente inteligente que atua como **Cliente MCP**. Ele se conecta a um servidor MCP (como o `mcp-booking-server`) para descobrir e utilizar as ferramentas de reservas de forma dinâmica, além de contar com capacidades avançadas de RAG via pgvector.

📖 [README completo do projeto →](./travel-agency-ai-mcp/README.md)

---

## 📦 travel-agency-ai-mcp-guardrails

> **Caminho:** `./travel-agency-ai-mcp-guardrails`

### O que é?

O agente de viagens mais seguro e resiliente da série. Combina as funcionalidades de Cliente MCP com implementações rígidas de **AI Guardrails** (como ToneGuardrail, InjectionGuard, e JsonStructureGuard), garantindo que as interações sejam sempre seguras, profissionais e aderentes ao formato de dados estipulado.

📖 [README completo do projeto →](./travel-agency-ai-mcp-guardrails/README.md)

---

## 🎓 Jornada de Aprendizado

Os projetos foram construídos em progressão para explorar os conceitos de IA aplicada em Java de forma gradual:

```
agencia-viagem-ai   →  agencia-viagem-ai-pgvector  →  agencia-viagem-ai-agents  →  agencia-viagem-ai-agents-with-session
      ⭐                         ⭐⭐                          ⭐⭐⭐                              ⭐⭐⭐⭐

  Easy RAG               RAG manual                  RAG + Agents               RAG + Agents + Sessão
  In-memory              PostgreSQL                   @Tool + Memory             SecurityContext
  Zero infra             pgvector                     ReAct Pattern              Auth por Header
                                                                                 Memória por Usuário
                                                                                 Filtro por Categoria

                                           [Nova Arquitetura Distribuída via MCP]

mcp-booking-server  →  travel-agency-ai-mcp  →  travel-agency-ai-mcp-guardrails
     ⭐⭐                      ⭐⭐⭐                           ⭐⭐⭐⭐

 Servidor MCP            Cliente MCP                  Cliente MCP Seguro
                         Descobre Tools via rede      Validadores Interceptores
                                                      (Guardrails)
```

Recomendado estudar nessa ordem para absorver os conceitos de forma incremental.

---

## 🔗 Referências

- [Quarkus — Getting Started](https://quarkus.io/get-started/)
- [Quarkus LangChain4j — Documentação](https://docs.quarkiverse.io/quarkus-langchain4j/dev/index.html)
- [LangChain4j — Documentação oficial](https://docs.langchain4j.dev/)
- [Ollama — Modelos disponíveis](https://ollama.com/search)
- [pgvector — Repositório](https://github.com/pgvector/pgvector)
- [Projeto de referência — enterprise-ai-java-langchain4j](https://github.com/eldermoraes/enterprise-ai-java-langchain4j)

---

<div align="center">
  <sub>Feito com ☕ Java, 🤖 IA e muito Quarkus</sub>
</div>
