# 🛡️ travel-agency-ai-guardrails

> **Caminho:** `./travel-agency-ai-mcp-guardrails`

Este projeto demonstra o uso de **Quarkus** com integração **LangChain4j**, abordando o uso de **Model Context Protocol (MCP)** e validações de segurança da IA (**Guardrails**) para assegurar uma interação de IA segura e previsível.

---

## 🛠️ Sobre o Quarkus

Este projeto utiliza o **Quarkus**, o framework Java supersônico e subatômico.
Se você quiser aprender mais sobre o Quarkus, visite o site oficial: [https://quarkus.io/](https://quarkus.io/).

### 🏃‍♂️ Rodando a aplicação em Modo de Desenvolvimento (Dev Mode)

Você pode executar sua aplicação no modo de desenvolvimento, que habilita o *live coding* usando:

```shell script
./mvnw quarkus:dev
```

> **NOTA:** O Quarkus agora vem com uma Dev UI, que está disponível apenas no modo de desenvolvimento em [http://localhost:8080/q/dev/](http://localhost:8080/q/dev/).

### 📦 Empacotando e Executando a Aplicação

A aplicação pode ser empacotada usando:

```shell script
./mvnw package
```

Isso produz o arquivo `quarkus-run.jar` no diretório `target/quarkus-app/`.
Esteja ciente de que não é um _über-jar_, pois as dependências são copiadas para o diretório `target/quarkus-app/lib/`.

A aplicação agora pode ser executada com o seguinte comando:
```shell script
java -jar target/quarkus-app/quarkus-run.jar
```

Se você quiser compilar um _über-jar_, execute o seguinte comando:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

A aplicação, empacotada como um _über-jar_, pode ser executada usando `java -jar target/*-runner.jar`.

### 🚀 Criando um Executável Nativo

Você pode criar um executável nativo usando:

```shell script
./mvnw package -Dnative
```

Ou, se você não tem o GraalVM instalado, pode executar o build nativo em um container usando:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

Em seguida, você pode executar o executável nativo com: `./target/travel-agency-ai-guardrails-1.0.0-SNAPSHOT-runner`

Se quiser saber mais sobre a construção de executáveis nativos, consulte a [documentação do Maven Tooling](https://quarkus.io/guides/maven-tooling).

---

## 📚 Guias Relacionados

- **LangChain4j Ollama** ([Guia](https://docs.quarkiverse.io/quarkus-langchain4j/dev/guide-ollama.html)): Fornece a integração básica do Ollama com o LangChain4j.
- **LangChain4j Model Context Protocol client** ([Guia](https://docs.quarkiverse.io/quarkus-langchain4j/dev/index.html)): Fornece a implementação client-side do Model Context Protocol para o LangChain4j.
- **ArC** ([Guia](https://quarkus.io/guides/cdi-reference)): Implementação CDI Lite orientada para build-time para Jakarta Contexts and Dependency Injection.
- **LangChain4j pgvector embedding store** ([Guia](https://docs.quarkiverse.io/quarkus-langchain4j/dev/index.html)): Fornece o store de Embeddings do pgvector para o Quarkus LangChain4j.
- **REST** ([Guia](https://quarkus.io/guides/rest)): Construa web services RESTful e APIs usando Jakarta REST (anteriormente JAX-RS).

## 💻 Código Fornecido

### REST

Inicie facilmente seus Web Services REST.
[Seção do guia relacionada...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)

![MCP Guardrails Setup](<Screenshot from 2026-06-02 01-58-52.png>)

<div align="center">
  <sub>Feito com ☕ Java, 🤖 IA e Quarkus</sub>
</div>