# Arquitetura Hexagonal - Guia de Implementação

## 📐 Visão Geral

Este documento descreve como implementar funcionalidades seguindo a Arquitetura Hexagonal neste projeto.

## 🎯 Estrutura Detalhada

### 1. Domain (Domínio) - `/domain`

O núcleo da aplicação. Contém apenas lógica de negócio pura, sem dependências externas.

#### `/domain/model`
Entidades de domínio com regras de negócio.

**Exemplo:**
```java
package com.inventory.domain.model;

import lombok.Builder;
import lombok.Value;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class Product {
    Long id;
    String sku;
    String name;
    String description;
    BigDecimal price;
    Integer quantity;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    
    public boolean isInStock() {
        return quantity != null && quantity > 0;
    }
    
    public void validateStock(Integer requestedQuantity) {
        if (!isInStock() || quantity < requestedQuantity) {
            throw new InsufficientStockException(
                "Estoque insuficiente para o produto: " + sku
            );
        }
    }
}
```

#### `/domain/event`
Eventos de domínio que representam algo que aconteceu.

**Exemplo:**
```java
package com.inventory.domain.event;

import lombok.Value;
import java.time.LocalDateTime;

@Value
public class ProductCreatedEvent {
    String eventId;
    String productId;
    String sku;
    LocalDateTime occurredAt;
    
    public static ProductCreatedEvent of(String productId, String sku) {
        return new ProductCreatedEvent(
            UUID.randomUUID().toString(),
            productId,
            sku,
            LocalDateTime.now()
        );
    }
}
```

#### `/domain/policy`
Políticas de negócio complexas.

**Exemplo:**
```java
package com.inventory.domain.policy;

public class StockReservationPolicy {
    
    public boolean canReserveStock(Product product, Integer quantity) {
        if (product.getQuantity() < quantity) {
            return false;
        }
        
        // Política: Sempre manter 10% de estoque de segurança
        int safetyStock = (int) Math.ceil(product.getQuantity() * 0.1);
        return (product.getQuantity() - quantity) >= safetyStock;
    }
}
```

#### `/domain/service`
Serviços de domínio para lógica que não pertence a uma entidade específica.

**Exemplo:**
```java
package com.inventory.domain.service;

public class PricingService {
    
    public BigDecimal calculateDiscountedPrice(
        BigDecimal originalPrice, 
        Integer quantity
    ) {
        // Política de desconto por quantidade
        if (quantity >= 100) {
            return originalPrice.multiply(BigDecimal.valueOf(0.85));
        } else if (quantity >= 50) {
            return originalPrice.multiply(BigDecimal.valueOf(0.90));
        }
        return originalPrice;
    }
}
```

#### `/domain/exception`
Exceções de domínio.

**Exemplo:**
```java
package com.inventory.domain.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String message) {
        super(message);
    }
}
```

---

### 2. Application (Aplicação) - `/application`

Camada de orquestração que coordena o fluxo da aplicação.

#### `/application/port/input`
Interfaces de casos de uso (o que a aplicação faz).

**Exemplo:**
```java
package com.inventory.application.port.input;

public interface CreateProductUseCase {
    ProductResponse execute(CreateProductCommand command);
}

@Value
class CreateProductCommand {
    String sku;
    String name;
    String description;
    BigDecimal price;
    Integer initialQuantity;
}

@Value
class ProductResponse {
    Long id;
    String sku;
    String name;
    LocalDateTime createdAt;
}
```

#### `/application/port/output`
Interfaces para adaptadores de saída.

**Exemplo:**
```java
package com.inventory.application.port.output;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(Long id);
    Optional<Product> findBySku(String sku);
    List<Product> findAll();
    void delete(Long id);
}

public interface EventPublisher {
    void publish(DomainEvent event);
}
```

#### `/application/service`
Implementação dos casos de uso.

**Exemplo:**
```java
package com.inventory.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateProductService implements CreateProductUseCase {
    
    private final ProductRepository productRepository;
    private final EventPublisher eventPublisher;
    
    @Override
    @Transactional
    public ProductResponse execute(CreateProductCommand command) {
        // 1. Validar regras de negócio
        validateUniqueSku(command.getSku());
        
        // 2. Criar entidade de domínio
        Product product = Product.builder()
            .sku(command.getSku())
            .name(command.getName())
            .description(command.getDescription())
            .price(command.getPrice())
            .quantity(command.getInitialQuantity())
            .createdAt(LocalDateTime.now())
            .build();
        
        // 3. Persistir
        Product savedProduct = productRepository.save(product);
        
        // 4. Publicar evento
        ProductCreatedEvent event = ProductCreatedEvent.of(
            savedProduct.getId().toString(),
            savedProduct.getSku()
        );
        eventPublisher.publish(event);
        
        // 5. Retornar resposta
        return new ProductResponse(
            savedProduct.getId(),
            savedProduct.getSku(),
            savedProduct.getName(),
            savedProduct.getCreatedAt()
        );
    }
    
    private void validateUniqueSku(String sku) {
        productRepository.findBySku(sku).ifPresent(p -> {
            throw new DuplicateSkuException("SKU já existe: " + sku);
        });
    }
}
```

---

### 3. Adapters (Adaptadores) - `/adapters`

Camada que conecta a aplicação com o mundo exterior.

#### `/adapters/input/rest`
Controllers REST que recebem requisições HTTP.

**Exemplo:**
```java
package com.inventory.adapters.input.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    
    private final CreateProductUseCase createProductUseCase;
    
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
        @Valid @RequestBody CreateProductRequest request
    ) {
        CreateProductCommand command = new CreateProductCommand(
            request.getSku(),
            request.getName(),
            request.getDescription(),
            request.getPrice(),
            request.getInitialQuantity()
        );
        
        ProductResponse response = createProductUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

@Data
class CreateProductRequest {
    @NotBlank
    private String sku;
    
    @NotBlank
    private String name;
    
    private String description;
    
    @NotNull
    @Positive
    private BigDecimal price;
    
    @Min(0)
    private Integer initialQuantity;
}
```

#### `/adapters/output/persistence`
Implementação JPA dos repositórios.

**Exemplo:**
```java
package com.inventory.adapters.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;

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
    
    @Override
    public Optional<Product> findById(Long id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomain);
    }
    
    @Override
    public Optional<Product> findBySku(String sku) {
        return jpaRepository.findBySku(sku)
            .map(mapper::toDomain);
    }
}

@Entity
@Table(name = "products")
@Data
class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String sku;
    
    @Column(nullable = false)
    private String name;
    
    private String description;
    
    @Column(nullable = false)
    private BigDecimal price;
    
    private Integer quantity;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

interface JpaProductRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findBySku(String sku);
}
```

#### `/adapters/output/messaging`
Publicação de eventos.

**Exemplo:**
```java
package com.inventory.adapters.output.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnsEventPublisher implements EventPublisher {
    
    private final SnsClient snsClient;
    private final String topicArn;
    private final ObjectMapper objectMapper;
    
    @Override
    public void publish(DomainEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            
            PublishRequest request = PublishRequest.builder()
                .topicArn(topicArn)
                .message(message)
                .subject(event.getClass().getSimpleName())
                .build();
            
            snsClient.publish(request);
            log.info("Evento publicado: {}", event.getClass().getSimpleName());
            
        } catch (Exception e) {
            log.error("Erro ao publicar evento", e);
            throw new EventPublishException("Falha ao publicar evento", e);
        }
    }
}
```

---

## 🔄 Fluxo de uma Requisição

```
1. HTTP Request
   ↓
2. Controller (adapters/input/rest)
   ↓
3. Use Case Interface (application/port/input)
   ↓
4. Service Implementation (application/service)
   ↓
5. Domain Logic (domain/model, domain/service)
   ↓
6. Output Port (application/port/output)
   ↓
7. Adapter Implementation (adapters/output/persistence)
   ↓
8. Database / External System
```

---

## ✅ Boas Práticas

### 1. Domain
- ✅ Sem dependências externas (Spring, JPA, etc.)
- ✅ Apenas lógica de negócio pura
- ✅ Use Value Objects e Records
- ✅ Valide invariantes no construtor

### 2. Application
- ✅ Um caso de uso = uma responsabilidade
- ✅ Transações gerenciadas aqui
- ✅ Orquestra domínio + portas
- ✅ Use Commands para entrada

### 3. Adapters
- ✅ Converta entre formatos (DTO ↔ Domain)
- ✅ Use MapStruct para conversões
- ✅ Tratamento de erros específicos da tecnologia
- ✅ Não exponha detalhes de implementação

---

## 🧪 Testabilidade

### Domain
```java
@Test
void shouldValidateInsufficientStock() {
    Product product = Product.builder()
        .sku("TEST-001")
        .quantity(5)
        .build();
    
    assertThrows(InsufficientStockException.class, 
        () -> product.validateStock(10));
}
```

### Application
```java
@Test
void shouldCreateProduct() {
    // Given
    ProductRepository mockRepo = mock(ProductRepository.class);
    EventPublisher mockPublisher = mock(EventPublisher.class);
    CreateProductService service = new CreateProductService(mockRepo, mockPublisher);
    
    // When
    CreateProductCommand command = new CreateProductCommand(...);
    ProductResponse response = service.execute(command);
    
    // Then
    verify(mockRepo).save(any(Product.class));
    verify(mockPublisher).publish(any(ProductCreatedEvent.class));
}
```

### Adapters
```java
@WebMvcTest(ProductController.class)
class ProductControllerTest {
    
    @MockBean
    private CreateProductUseCase createProductUseCase;
    
    @Test
    void shouldCreateProductViaRest() throws Exception {
        // Test REST endpoint
        mockMvc.perform(post("/api/v1/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "sku": "TEST-001",
                    "name": "Test Product",
                    "price": 99.99,
                    "initialQuantity": 100
                }
                """))
            .andExpect(status().isCreated());
    }
}
```

---

## 📚 Referências

- [Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)
- [Spring Boot Best Practices](https://spring.io/guides)
- [Domain-Driven Design](https://www.domainlanguage.com/ddd/)

