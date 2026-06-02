# 🏨 mcp-booking-server

> **Caminho:** `./mcp-booking-server`

Este projeto atua como um servidor MCP (Model Context Protocol) usando Quarkus, expondo capacidades de reserva (booking) como ferramentas que podem ser consumidas por agentes inteligentes.

---

## 🚀 Inicialização do Projeto

O projeto foi gerado com o comando abaixo, definindo as extensões base:

```shell script
mvn io.quarkus.platform:quarkus-maven-plugin:3.2.10.Final:create \
  -DprojectGroupId=dev.ia.travel \
  -DprojectArtifactId=mcp-booking-server \
  -DprojectVersion=1.0-SNAPSHOT \
  -Dextensions="quarkus-rest,quarkus-mcp-server-sse"
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

Em seguida, você pode executar o executável nativo com: `./target/mcp-booking-server-1.0-SNAPSHOT-runner`

Se quiser saber mais sobre a construção de executáveis nativos, consulte a [documentação do Maven Tooling](https://quarkus.io/guides/maven-tooling).

---

## 💻 Código Fornecido

### REST

Inicie facilmente seus Web Services REST.
[Seção do guia relacionada...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)

![Mcp Booking Server Architecture Screenshot](<Screenshot from 2026-05-31 23-39-41.png>)

<div align="center">
  <sub>Feito com ☕ Java, 🤖 IA e Quarkus</sub>
</div>