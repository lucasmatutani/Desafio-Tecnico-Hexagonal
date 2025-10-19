# Dockerfile para avaliação do projeto
# Simples e funcional - foco em facilitar a execução, não otimização para produção

FROM maven:3.9-eclipse-temurin-21-alpine

LABEL maintainer="Inventory Service"
LABEL description="Inventory Management Service - Docker para avaliação"

# Diretório de trabalho
WORKDIR /app

# Copiar arquivos do projeto
COPY pom.xml .
COPY src ./src

# Compilar o projeto (gera o JAR)
RUN mvn clean package -DskipTests

# Expor porta da aplicação
EXPOSE 8081

# Comando para executar a aplicação
CMD ["java", "-jar", "target/inventory-service-1.0.0.jar"]

