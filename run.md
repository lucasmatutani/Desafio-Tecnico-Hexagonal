# 🚀 Como Executar o Projeto

Este guia mostra como executar o **Inventory Management Service** usando **Docker**.

---

## Pré-requisitos

- 🐳 **Docker Desktop** ([Download](https://www.docker.com/products/docker-desktop))
- 💾 **~500MB** de espaço em disco

---

## 🐳 Executar com Docker

### Opção 1: Docker Compose (Recomendado) ⭐

```bash
# Subir a aplicação (build + run automático)
docker-compose up

# Ou em background
docker-compose up -d

# Ver logs
docker-compose logs -f

# Parar
docker-compose down
```

### Opção 2: Docker Build Manual

```bash
# Build da imagem
docker build -t inventory-service:latest .

# Executar container
docker run -p 8081:8081 inventory-service:latest
```

---

## 📍 Acessar a Aplicação

Após executar o Docker, a aplicação estará disponível em:

| Recurso | URL |
|---------|-----|
| **API REST** | http://localhost:8081 |
| **Swagger UI** | http://localhost:8081/swagger-ui.html |
| **H2 Console** | http://localhost:8081/h2-console |
| **Health Check** | http://localhost:8081/actuator/health |

---

## ✅ Verificar se Está Funcionando

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

## 🗄️ Acessar H2 Console

1. Acesse: http://localhost:8081/h2-console

2. **Configurações:**

```
JDBC URL: jdbc:h2:mem:inventory
User: sa
Password: (deixe vazio)
```

3. **Queries úteis:**

```sql
-- Ver todos os inventários
SELECT * FROM inventory;

-- Ver todas as reservas
SELECT * FROM reservation;

-- Ver histórico de eventos (Event Sourcing)
SELECT * FROM event ORDER BY timestamp DESC;
```

---

## 🔧 Comandos Úteis

### Gerenciamento de Containers

```bash
# Ver containers rodando
docker ps

# Ver logs em tempo real
docker logs -f inventory-service

# Acessar terminal do container
docker exec -it inventory-service sh

# Parar container
docker-compose down

# Parar e remover volumes
docker-compose down -v
```

### Rebuild e Limpeza

```bash
# Rebuild forçado (se mudou código)
docker-compose up --build

# Rebuild sem cache
docker-compose build --no-cache

# Limpar tudo (imagens, containers, volumes)
docker-compose down -v
docker rmi inventory-service
docker system prune -a
```

### Verificação

```bash
# Ver uso de recursos
docker stats inventory-service

# Inspecionar container
docker inspect inventory-service

# Ver logs dos últimos 100 linhas
docker logs --tail 100 inventory-service
```

---

## 🐛 Troubleshooting

### Problema: Porta 8081 já está em uso

**Erro:**
```
Error starting userland proxy: listen tcp4 0.0.0.0:8081: bind: address already in use
```

**Solução 1 - Parar o que está usando a porta:**
```bash
# Linux/Mac - Encontrar e matar processo
lsof -ti:8081 | xargs kill -9

# Windows - Encontrar processo
netstat -ano | findstr :8081
# Depois matar pelo PID
taskkill /PID <PID> /F
```

**Solução 2 - Mudar a porta no docker-compose.yml:**
```yaml
ports:
  - "8082:8081"  # Porta 8082 no host, 8081 no container
```

### Problema: Docker não está rodando

**Erro:**
```
Cannot connect to the Docker daemon
```

**Solução:**
- Abra o Docker Desktop
- Aguarde iniciar completamente
- Tente novamente

### Problema: Build muito lento

**Solução:**
```bash
# Limpar cache do Docker
docker builder prune

# Rebuild sem cache
docker-compose build --no-cache
```

### Problema: Container não inicia

**Ver logs detalhados:**
```bash
docker-compose logs inventory-service

# Ou logs completos
docker logs inventory-service
```

**Verificar se tem erros de compilação:**
```bash
# Rebuild com saída completa
docker-compose up --build
```

### Problema: "Out of memory" durante build

**Solução:**
```bash
# Aumentar memória do Docker Desktop
# Settings → Resources → Memory → 4GB ou mais
```

### Problema: Mudanças no código não aparecem

**Solução:**
```bash
# Rebuild forçado
docker-compose down
docker-compose up --build
```

---

## 📝 Variáveis de Ambiente

Você pode customizar a aplicação editando o `docker-compose.yml`:

```yaml
environment:
  # Porta da aplicação
  - SERVER_PORT=8081
  
  # Profile (default, test, prod)
  - SPRING_PROFILES_ACTIVE=default
  
  # Database
  - SPRING_DATASOURCE_URL=jdbc:h2:mem:inventory
  - SPRING_DATASOURCE_USERNAME=sa
  - SPRING_DATASOURCE_PASSWORD=
  
  # Logs
  - LOGGING_LEVEL_ROOT=INFO
  - LOGGING_LEVEL_COM_INVENTORY=DEBUG
  - LOGGING_LEVEL_ORG_HIBERNATE_SQL=INFO
```

### Exemplo: Mudar porta

```yaml
environment:
  - SERVER_PORT=9090
ports:
  - "9090:9090"  # Mudar ambas as portas
```

### Exemplo: Desabilitar logs SQL

```yaml
environment:
  - LOGGING_LEVEL_ORG_HIBERNATE_SQL=WARN
```

---

## 🧪 Executar Testes (Dentro do Container)

### Opção 1: Usar imagem Maven

```bash
# Executar todos os testes
docker run --rm -v $(pwd):/app -w /app maven:3.9-eclipse-temurin-21 mvn test

# Apenas unit tests
docker run --rm -v $(pwd):/app -w /app maven:3.9-eclipse-temurin-21 mvn test -Dtest="*Test"

# Com relatório de cobertura
docker run --rm -v $(pwd):/app -w /app maven:3.9-eclipse-temurin-21 mvn clean test jacoco:report
```

### Opção 2: Dentro do container em execução

```bash
# Acessar o container
docker exec -it inventory-service sh

# Dentro do container
cd /app
mvn test
```

**Nota:** Para desenvolvimento com testes frequentes, considere rodar localmente com Maven para ter feedback mais rápido.

---

## 🎯 Próximos Passos

Após executar o projeto:

1. ✅ Explore o **Swagger UI**: http://localhost:8081/swagger-ui.html
2. ✅ Teste os endpoints via Swagger ou `curl`
3. ✅ Inspecione o banco H2: http://localhost:8081/h2-console
4. ✅ Veja os logs: `docker-compose logs -f`
5. ✅ Consulte a [Documentação de Arquitetura](ARCHITECTURE.md)

---

## 📚 Documentação Adicional

- [README.md](README.md) - Visão geral do projeto
- [API Documentation](http://localhost:8081/swagger-ui.html) - Documentação interativa (após executar)

---

## 💡 Dicas

### Para Desenvolvimento

Se você vai desenvolver/modificar o código:

1. **Monte o código como volume** no docker-compose.yml:
   ```yaml
   volumes:
     - ./src:/app/src
   ```

2. **Use Spring DevTools** para hot reload (já incluído no pom.xml)

3. **Ou rode localmente** para feedback mais rápido:
   ```bash
   mvn spring-boot:run
   ```

### Para Avaliação/Demo

Se você só quer rodar para testar:

1. **Use docker-compose** (mais simples)
2. **Acesse o Swagger** para testar todos os endpoints
3. **Consulte o H2 Console** para ver os dados

### Para CI/CD

Para integração contínua:

```bash
# Build e teste em uma linha
docker-compose up -d && \
docker-compose exec inventory-service mvn test && \
docker-compose down
```

---

## 📊 Recursos do Container

O container está configurado com:

- **Imagem Base:** `maven:3.9-eclipse-temurin-21-alpine`
- **Tamanho:** ~500MB
- **Memória:** Sem limite (use Docker Settings se precisar)
- **CPU:** Sem limite
- **Rede:** `inventory-network` (bridge)
- **Healthcheck:** Verifica `/actuator/health` a cada 30s

---

## 🔍 Logs Estruturados

Os logs seguem o padrão:

```
timestamp [thread] LEVEL logger - message

Exemplo:
2025-10-19 14:00:00 [http-nio-8081-exec-1] DEBUG ReserveStockService - Reserving 10 units of SKU123
```

**Ver logs filtrados:**
```bash
# Apenas DEBUG da aplicação
docker logs inventory-service 2>&1 | grep DEBUG

# Apenas erros
docker logs inventory-service 2>&1 | grep ERROR

# Últimas 50 linhas
docker logs --tail 50 inventory-service
```

---

**🎉 Projeto executando com sucesso! Acesse http://localhost:8081/swagger-ui.html para começar!**
