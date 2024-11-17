# Usar uma imagem base do Java 17
FROM openjdk:17-jdk-slim

# Diretorio de trabalho no container
WORKDIR /app

# Copiar o arquivo JAR gerado para o container
COPY target/card-game-api-0.0.1-SNAPSHOT.jar app.jar

# Expor a porta 8080 para o host
EXPOSE 8080

# Comando para executar o JAR da aplicacao
ENTRYPOINT ["java", "-jar", "app.jar"]
