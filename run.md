# 🚀 Como Executar o Projeto

Este guia mostra como executar o **Inventory Management Service** localmente.

---

## Pré-requisitos

- ☕ **Java 21+** ([Download](https://adoptium.net/))
- 📦 **Maven 3.9+** ([Download](https://maven.apache.org/download.cgi))

---

## 1 - Compile o Projeto

```bash
mvn clean install
```

**Saída esperada:**
```
[INFO] BUILD SUCCESS
[INFO] Total time: 30.123 s
```

---

## 2 - Execute a Aplicação

```bash
mvn spring-boot:run
```

**A aplicação estará disponível em:**

| Recurso | URL |
|---------|-----|
| **API REST** | http://localhost:8081 |
| **Swagger UI** | http://localhost:8081/swagger-ui.html |
| **H2 Console** | http://localhost:8081/h2-console |
| **Health Check** | http://localhost:8081/actuator/health |

---

## 3 - Acessar H2 Console (Dev Mode)

Acesse: http://localhost:8081/h2-console

**Configurações:**

```
JDBC URL: jdbc:h2:mem:inventory
User: sa
Password: (deixe vazio)
```

**Queries úteis:**

```sql
-- Ver todos os inventários
SELECT * FROM inventory;

-- Ver todas as reservas
SELECT * FROM reservation;

-- Ver histórico de eventos
SELECT * FROM event ORDER BY timestamp DESC;
```

---

## 4 - Executar Testes

### Todos os Testes

```bash
mvn test
```

**Saída esperada:**
```
Tests run: 78, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

### Apenas Unit Tests

```bash
mvn test -Dtest="*Test"
```

### Apenas Integration Tests

```bash
mvn test -Dtest="*IntegrationTest"
```

### Apenas Architecture Tests

```bash
mvn test -Dtest="HexagonalArchitectureTest"
```

### Com Relatório de Cobertura

```bash
mvn clean test jacoco:report
```

**Relatório gerado em:** `target/site/jacoco/index.html`

**Abrir relatório:**
```bash
# Linux/Mac
open target/site/jacoco/index.html

# Windows
start target/site/jacoco/index.html
```

---

## 5 - Build para Produção

### Gerar JAR

```bash
mvn clean package -DskipTests
```

**JAR gerado em:** `target/inventory-service-1.0.0.jar`

### Executar JAR

```bash
java -jar target/inventory-service-1.0.0.jar
```

### Executar com Profile Específico

```bash
# Profile de teste
java -jar target/inventory-service-1.0.0.jar --spring.profiles.active=test

# Profile de produção (futuro)
java -jar target/inventory-service-1.0.0.jar --spring.profiles.active=prod
```

---

## 🧪 Verificar se Está Funcionando

### 1. Health Check

```bash
curl http://localhost:8081/actuator/health
```

**Response esperado:**
```json
{
  "status": "UP"
}
```

### 2. Consultar Estoque

```bash
curl http://localhost:8081/api/v1/inventory/STORE-01/SKU123
```

**Response esperado:**
```json
{
  "storeId": "STORE-01",
  "sku": "SKU123",
  "productName": "Notebook Dell XPS 13",
  "availableStock": 100,
  "reservedStock": 0,
  "soldStock": 0,
  "totalStock": 100
}
```

### 3. Reservar Estoque

```bash
curl -X POST http://localhost:8081/api/v1/inventory/reserve \
  -H "Content-Type: application/json" \
  -d '{
    "storeId": "STORE-01",
    "sku": "SKU123",
    "quantity": 10,
    "customerId": "CUST-001"
  }'
```

**Response esperado:**
```json
{
  "reservationId": "RES-...",
  "storeId": "STORE-01",
  "sku": "SKU123",
  "quantity": 10,
  "status": "RESERVED",
  "expiresAt": "2025-10-19T14:15:00Z"
}
```

---

## 🐛 Troubleshooting

### Problema: Porta 8081 já está em uso

**Erro:**
```
The Tomcat connector configured to listen on port 8081 failed to start. The port may already be in use...
```

**Solução 1 - Mudar a porta:**
```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8082
```

**Solução 2 - Matar o processo:**
```bash
# Linux/Mac
lsof -ti:8081 | xargs kill -9

# Windows
netstat -ano | findstr :8081
taskkill /PID <PID> /F
```

### Problema: Maven não encontrado

**Erro:**
```
mvn: command not found
```

**Solução:**
```bash
# Linux/Mac
brew install maven  # macOS
sudo apt install maven  # Ubuntu/Debian

# Ou baixar manualmente:
# https://maven.apache.org/download.cgi
```

### Problema: Java não encontrado ou versão incorreta

**Erro:**
```
java: command not found
```

**Solução:**
```bash
# Verificar versão
java -version

# Deve mostrar Java 21+
# Se não, instale Java 21:
# https://adoptium.net/
```

### Problema: Testes falhando

**Erro:**
```
Tests run: 78, Failures: 2, Errors: 1
```

**Solução:**
```bash
# Limpar e recompilar
mvn clean install

# Se ainda falhar, pular testes no build
mvn clean install -DskipTests

# Depois executar testes isoladamente
mvn test
```

### Problema: H2 Console não abre

**Erro:**
```
404 Not Found
```

**Solução:**
Verificar se a aplicação está rodando:
```bash
curl http://localhost:8081/actuator/health
```

Se não estiver, iniciar com:
```bash
mvn spring-boot:run
```

---

## 🔄 Hot Reload (Desenvolvimento)

Para reiniciar automaticamente a aplicação quando houver mudanças:

### 1. Adicionar Spring DevTools (já incluído)

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <optional>true</optional>
</dependency>
```

### 2. Executar em modo Dev

```bash
mvn spring-boot:run
```

**Agora qualquer mudança em arquivos `.java` reiniciará automaticamente a aplicação!**

---

## 🐳 Docker (Opcional)

Se preferir executar via Docker:

### Build da Imagem

```bash
docker build -t inventory-service:latest .
```

### Executar Container

```bash
docker run -p 8081:8081 inventory-service:latest
```

### Docker Compose (com PostgreSQL)

```bash
docker-compose up -d
```

---

## 📝 Variáveis de Ambiente

Configurações que podem ser customizadas:

| Variável | Padrão | Descrição |
|----------|--------|-----------|
| `SERVER_PORT` | 8081 | Porta da aplicação |
| `SPRING_PROFILES_ACTIVE` | default | Profile ativo (test, prod) |
| `H2_CONSOLE_ENABLED` | true | Habilitar H2 Console |
| `LOGGING_LEVEL_ROOT` | INFO | Nível de log global |
| `LOGGING_LEVEL_COM_INVENTORY` | DEBUG | Nível de log da aplicação |

**Exemplo:**
```bash
export SERVER_PORT=9090
export SPRING_PROFILES_ACTIVE=test
mvn spring-boot:run
```

---

## 🎯 Próximos Passos

Após executar o projeto:

1. ✅ Explore o **Swagger UI**: http://localhost:8081/swagger-ui.html
2. ✅ Teste os endpoints via Swagger ou `curl`
3. ✅ Inspecione o banco H2: http://localhost:8081/h2-console
4. ✅ Execute os testes: `mvn test`
5. ✅ Gere o relatório de cobertura: `mvn clean test jacoco:report`

---

## 📚 Documentação Adicional

- [README.md](README.md) - Visão geral do projeto
- [ARCHITECTURE.md](ARCHITECTURE.md) - Arquitetura detalhada
- [API Documentation](http://localhost:8081/swagger-ui.html) - Documentação interativa (após executar)

---

**🎉 Projeto executando com sucesso! Acesse http://localhost:8081/swagger-ui.html para começar!**

