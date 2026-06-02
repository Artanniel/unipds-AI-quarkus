# 🤖 travel-agency-ai-mcp

> **Caminho:** `./travel-agency-ai-mcp`

Este projeto demonstra como construir um agente inteligente com **Quarkus** integrado com **LangChain4j**, **Ollama**, **pgvector** e recursos de **MCP (Model Context Protocol)**.

---

## 🚀 Inicialização do Projeto

O projeto foi gerado com o comando abaixo, definindo as extensões base:

```shell script
mvn io.quarkus.platform:quarkus-maven-plugin:3.26.3:create \
  -DprojectGroupId=dev.ia \
  -DprojectArtifactId=travel-agency-ai-mcp \
  -DprojectVersion=1.0.0-SNAPSHOT \
  -Dextensions="quarkus-rest,quarkus-arc,quarkus-langchain4j-ollama,quarkus-langchain4j-pgvector,quarkus-langchain4j-mcp"
```

Para adicionar a extensão que permite comunicação com outro servidor MCP, utilize:

```shell script
mvn quarkus:add-extension -Dextensions="quarkus-langchain4j-mcp"
```

---

## 🛠️ Sobre o Quarkus

Este projeto utiliza o **Quarkus**, o framework Java supersônico e subatômico.
Se você quiser aprender mais sobre o Quarkus, visite o site oficial: [https://quarkus.io/](https://quarkus.io/).

### 🏃‍♂️ Rodando a aplicação em Modo de Desenvolvimento (Dev Mode)

Você pode executar sua aplicação no modo de desenvolvimento, que habilita o *live coding* usando:

```shell script
./mvnw compile quarkus:dev
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
./mvnw package -Dquarkus.package.type=uber-jar
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

Em seguida, você pode executar o executável nativo com: `./target/agencia-viagem-ai-1.0.0-SNAPSHOT-runner`

Se quiser saber mais sobre a construção de executáveis nativos, consulte a [documentação do Maven Tooling](https://quarkus.io/guides/maven-tooling).

---

## 📚 Guias Relacionados

- **LangChain4j Ollama** ([Guia](https://docs.quarkiverse.io/quarkus-langchain4j/dev/index.html)): Integração básica do Ollama com o LangChain4j.
- **REST** ([Guia](https://quarkus.io/guides/rest)): Uma implementação do Jakarta REST utilizando processamento em tempo de build e Vert.x. *Esta extensão não é compatível com o quarkus-resteasy, ou qualquer outra extensão que dependa dele.*

## 💻 Código Fornecido

### REST

Inicie facilmente seus Web Services REST.
[Seção do guia relacionada...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)

<div align="center">
  <sub>Feito com ☕ Java, 🤖 IA e Quarkus</sub>
</div>