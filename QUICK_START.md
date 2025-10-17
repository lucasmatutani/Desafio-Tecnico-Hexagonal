# 🚀 Quick Start - Inventory Service

## Início Rápido

### 1️⃣ Executar a Aplicação

```bash
mvn spring-boot:run
```

A aplicação estará disponível em: **http://localhost:8081**

### 2️⃣ Acessos Rápidos

| Serviço | URL | Credenciais |
|---------|-----|-------------|
| **API** | http://localhost:8081 | - |
| **H2 Console** | http://localhost:8081/h2-console | URL: `jdbc:h2:mem:inventory`<br>User: `sa`<br>Pass: (vazio) |
| **Swagger** | http://localhost:8081/swagger-ui.html | - |
| **Health Check** | http://localhost:8081/actuator/health | - |
| **Metrics** | http://localhost:8081/actuator/prometheus | - |

### 3️⃣ Comandos Maven Úteis

```bash
# Limpar e compilar
mvn clean compile

# Executar testes
mvn test

# Executar com perfil específico
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Criar JAR
mvn clean package

# Executar JAR
java -jar target/inventory-service-1.0.0.jar
```

### 4️⃣ Verificar Estrutura

```bash
./verify-structure.sh
```

## 📁 Onde Implementar Cada Funcionalidade

### Para Criar uma Nova Funcionalidade

**Exemplo: Gerenciar Produtos**

#### 1. Domain Layer (`domain/`)

```java
// domain/model/Product.java
public class Product {
    // Entidade de domínio pura
}

// domain/event/ProductCreatedEvent.java
public record ProductCreatedEvent(String productId, LocalDateTime occurredAt) {}

// domain/exception/ProductNotFoundException.java
public class ProductNotFoundException extends RuntimeException {}
```

#### 2. Application Layer (`application/`)

```java
// application/port/input/CreateProductUseCase.java
public interface CreateProductUseCase {
    ProductResponse execute(CreateProductCommand command);
}

// application/port/output/ProductRepository.java
public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(Long id);
}

// application/service/CreateProductService.java
@Service
public class CreateProductService implements CreateProductUseCase {
    // Implementação do caso de uso
}
```

#### 3. Adapters Layer (`adapters/`)

```java
// adapters/input/rest/ProductController.java
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    // Endpoints REST
}

// adapters/output/persistence/ProductRepositoryAdapter.java
@Component
public class ProductRepositoryAdapter implements ProductRepository {
    // Implementação JPA
}

// adapters/output/messaging/ProductEventPublisher.java
@Component
public class ProductEventPublisher implements EventPublisher {
    // Publicação de eventos
}
```

## 🎯 Template de Implementação

### Controller REST

```java
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    
    private final CreateProductUseCase createProductUseCase;
    
    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody CreateProductRequest request) {
        CreateProductCommand command = // map request to command
        ProductResponse response = createProductUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
```

### Use Case

```java
@Service
@RequiredArgsConstructor
@Transactional
public class CreateProductService implements CreateProductUseCase {
    
    private final ProductRepository repository;
    private final EventPublisher eventPublisher;
    
    @Override
    public ProductResponse execute(CreateProductCommand command) {
        // 1. Validar
        // 2. Criar entidade de domínio
        // 3. Persistir
        // 4. Publicar evento
        // 5. Retornar resposta
    }
}
```

### Repository Adapter

```java
@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {
    
    private final JpaProductRepository jpaRepository;
    private final ProductMapper mapper;
    
    @Override
    public Product save(Product product) {
        ProductEntity entity = mapper.toEntity(product);
        ProductEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
}
```

## 🔍 Padrões de Código

### Nomenclatura

- **Entidades**: `Product`, `Inventory`
- **DTOs**: `ProductRequest`, `ProductResponse`
- **Commands**: `CreateProductCommand`, `UpdateStockCommand`
- **Events**: `ProductCreatedEvent`, `StockUpdatedEvent`
- **Use Cases**: `CreateProductUseCase`, `UpdateStockUseCase`
- **Services**: `CreateProductService`, `UpdateStockService`
- **Controllers**: `ProductController`, `InventoryController`
- **Repositories**: `ProductRepository`, `InventoryRepository`
- **Adapters**: `ProductRepositoryAdapter`, `SnsEventPublisher`

### Pacotes

```
com.inventory.domain.model         → Entidades
com.inventory.domain.event         → Eventos
com.inventory.application.port.input  → Use Cases
com.inventory.application.service     → Implementações
com.inventory.adapters.input.rest     → Controllers
com.inventory.adapters.output.persistence → Repositórios
```

## 🧪 Testes

### Estrutura de Testes

```
src/test/java/com/inventory/
├── domain/
│   ├── model/ProductTest.java
│   └── service/PricingServiceTest.java
├── application/
│   └── service/CreateProductServiceTest.java
└── adapters/
    ├── input/rest/ProductControllerTest.java
    └── output/persistence/ProductRepositoryAdapterTest.java
```

### Exemplo de Teste Unitário

```java
@Test
void shouldCreateProduct() {
    // Given
    CreateProductCommand command = new CreateProductCommand(...);
    
    // When
    ProductResponse response = service.execute(command);
    
    // Then
    assertThat(response.getId()).isNotNull();
    verify(repository).save(any(Product.class));
}
```

## 📊 Monitoramento

### Logs

```bash
# Ver logs em tempo real
tail -f logs/inventory-service.log

# Filtrar logs por nível
grep "ERROR" logs/inventory-service.log
```

### Métricas

```bash
# Ver todas as métricas
curl http://localhost:8081/actuator/metrics

# Ver métrica específica
curl http://localhost:8081/actuator/metrics/http.server.requests
```

### Health Check

```bash
curl http://localhost:8081/actuator/health
```

## 🐛 Troubleshooting

### Problema: Porta 8081 já está em uso

```bash
# Linux/Mac
lsof -i :8081
kill -9 <PID>

# Ou altere a porta em application.yml
server:
  port: 8082
```

### Problema: Dependências não encontradas

```bash
mvn clean install -U
```

### Problema: Erro de compilação

```bash
mvn clean compile
mvn dependency:tree
```

## 📚 Documentação Adicional

- **README.md** - Visão geral completa
- **ARCHITECTURE.md** - Guia detalhado de arquitetura
- **STRUCTURE.txt** - Visualização da estrutura

## 🎓 Recursos de Aprendizado

- [Arquitetura Hexagonal](https://alistair.cockburn.us/hexagonal-architecture/)
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Domain-Driven Design](https://www.domainlanguage.com/ddd/)

---

**Versão**: 1.0.0  
**Última Atualização**: 2025-10-17

