# Usar uma imagem base do Maven para construir o projeto
FROM openjdk:17-slim AS build

# Instalar Maven
RUN apt-get update && apt-get install -y maven

# Definir diretório de trabalho no container
WORKDIR /app

# Copiar o arquivo pom.xml e baixar as dependências
COPY pom.xml .

# Baixar dependências para cache de camada
RUN mvn dependency:go-offline

# Copiar o código fonte
COPY src /app/src

# Compilar o código e gerar o JAR
RUN mvn clean package -DskipTests

# Imagem final para rodar a aplicação
FROM openjdk:17-jdk-slim

# Definir diretório de trabalho no container
WORKDIR /app

# Copiar o JAR gerado do estágio de build
COPY --from=build /app/target/card-game-api-0.0.1-SNAPSHOT.jar app.jar

# Expor a porta 8080 para o host
EXPOSE 8080

# Definir o comando para executar o JAR da aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]