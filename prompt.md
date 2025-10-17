Crie a estrutura inicial completa de um projeto Spring Boot seguindo Arquitetura Hexagonal para um sistema de gerenciamento de inventário.

REQUISITOS TÉCNICOS:
- Spring Boot 3.4.0
- Java 21
- Maven
- Arquitetura Hexagonal (Ports & Adapters)

ESTRUTURA DE PASTAS:

Crie a seguinte estrutura EXATA de pacotes em src/main/java/com/inventory/:

├── adapters/
│   ├── input/
│   │   └── rest/
│   └── output/
│       ├── persistence/
│       └── messaging/
├── application/
│   ├── port/
│   │   ├── input/
│   │   └── output/
│   └── service/
└── domain/
    ├── model/
    ├── event/
    ├── policy/
    ├── service/
    └── exception/

POM.XML:

<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.4.0</version>
        <relativePath/>
    </parent>
    
    <groupId>com.inventory</groupId>
    <artifactId>inventory-service</artifactId>
    <version>1.0.0</version>
    <name>Inventory Service</name>
    <description>Inventory Management Service with Event Sourcing</description>
    
    <properties>
        <java.version>21</java.version>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <mapstruct.version>1.5.5.Final</mapstruct.version>
        <aws-sdk.version>2.20.0</aws-sdk.version>
        <archunit.version>1.2.1</archunit.version>
    </properties>
    
    <dependencies>
        <!-- Spring Boot -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        
        <!-- Database -->
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>
        
        <!-- Lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        
        <!-- MapStruct -->
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
            <version>${mapstruct.version}</version>
        </dependency>
        
        <!-- AWS SDK -->
        <dependency>
            <groupId>software.amazon.awssdk</groupId>
            <artifactId>sqs</artifactId>
            <version>${aws-sdk.version}</version>
        </dependency>
        
        <dependency>
            <groupId>software.amazon.awssdk</groupId>
            <artifactId>sns</artifactId>
            <version>${aws-sdk.version}</version>
        </dependency>
        
        <!-- Resilience4j -->
        <dependency>
            <groupId>io.github.resilience4j</groupId>
            <artifactId>resilience4j-spring-boot3</artifactId>
            <version>2.1.0</version>
        </dependency>
        
        <!-- Micrometer/Prometheus -->
        <dependency>
            <groupId>io.micrometer</groupId>
            <artifactId>micrometer-registry-prometheus</artifactId>
        </dependency>
        
        <!-- OpenAPI/Swagger -->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.3.0</version>
        </dependency>
        
        <!-- Jackson for JSON -->
        <dependency>
            <groupId>com.fasterxml.jackson.datatype</groupId>
            <artifactId>jackson-datatype-jsr310</artifactId>
        </dependency>
        
        <!-- Testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        
        <dependency>
            <groupId>com.tngtech.archunit</groupId>
            <artifactId>archunit-junit5</artifactId>
            <version>${archunit.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
            
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>21</source>
                    <target>21</target>
                    <annotationProcessorPaths>
                        <path>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                            <version>${lombok.version}</version>
                        </path>
                        <path>
                            <groupId>org.mapstruct</groupId>
                            <artifactId>mapstruct-processor</artifactId>
                            <version>${mapstruct.version}</version>
                        </path>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>

APPLICATION.YML (src/main/resources/application.yml):

spring:
  application:
    name: inventory-service
    
  datasource:
    url: jdbc:h2:mem:inventory
    driver-class-name: org.h2.Driver
    username: sa
    password: 
    
  h2:
    console:
      enabled: true
      path: /h2-console
      
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: create-drop
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        use_sql_comments: true
        
  jackson:
    serialization:
      write-dates-as-timestamps: false
    default-property-inclusion: non_null
    
server:
  port: 8081
  error:
    include-message: always
    include-binding-errors: always
    include-stacktrace: on_param
    include-exception: false
    
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  metrics:
    export:
      prometheus:
        enabled: true
        
logging:
  level:
    root: INFO
    com.inventory: DEBUG
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
    
aws:
  region: us-east-1
  endpoint: ${AWS_ENDPOINT:http://localhost:4566}
  sns:
    topic-arn: ${SNS_TOPIC_ARN:arn:aws:sns:us-east-1:000000000000:inventory-events}
  sqs:
    queue-url: ${SQS_QUEUE_URL:http://localhost:4566/000000000000/inventory-events}
    
resilience4j:
  circuitbreaker:
    instances:
      eventPublisher:
        sliding-window-size: 10
        failure-rate-threshold: 50
        wait-duration-in-open-state: 10s
        
  retry:
    instances:
      database:
        max-attempts: 3
        wait-duration: 100ms
        
  ratelimiter:
    instances:
      api:
        limit-for-period: 100
        limit-refresh-period: 1s

CLASSE PRINCIPAL (src/main/java/com/inventory/InventoryServiceApplication.java):

package com.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InventoryServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }
}

.GITIGNORE:

target/
!.mvn/wrapper/maven-wrapper.jar
!**/src/main/**/target/
!**/src/test/**/target/

### IntelliJ IDEA ###
.idea
*.iws
*.iml
*.ipr

### VS Code ###
.vscode/

### Mac ###
.DS_Store

### Logs ###
*.log

ARQUIVO VAZIO EM CADA PACOTE:

Crie um arquivo .gitkeep em cada pasta vazia para garantir que a estrutura seja versionada:
- adapters/input/rest/.gitkeep
- adapters/output/persistence/.gitkeep
- adapters/output/messaging/.gitkeep
- application/port/input/.gitkeep
- application/port/output/.gitkeep
- application/service/.gitkeep
- domain/model/.gitkeep
- domain/event/.gitkeep
- domain/policy/.gitkeep
- domain/service/.gitkeep
- domain/exception/.gitkeep

IMPORTANTE:
- Use EXATAMENTE Java 21 e Spring Boot 3.4.0
- Crie TODAS as pastas da estrutura hexagonal
- application.yml deve ter configuração completa
- pom.xml com TODAS as dependências necessárias

===============================================================================================================

## **📋 PROMPT 2: Domain Model - Value Objects**

Crie os Value Objects no pacote domain/model/.

IMPORTANTE:
- Value Objects são IMUTÁVEIS (use Java Records)
- Validações no construtor compacto
- Factory methods para criação
- toString() customizado

ARQUIVOS A CRIAR:

1. StoreId.java

package com.inventory.domain.model;

import java.util.Objects;

public record StoreId(String value) {
    
    public StoreId {
        Objects.requireNonNull(value, "StoreId cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("StoreId cannot be empty");
        }
        if (value.length() > 50) {
            throw new IllegalArgumentException("StoreId too long (max 50 chars)");
        }
    }
    
    public static StoreId of(String value) {
        return new StoreId(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}

2. Sku.java

package com.inventory.domain.model;

import java.util.Objects;
import java.util.regex.Pattern;

public record Sku(String value) {
    
    private static final Pattern SKU_PATTERN = Pattern.compile("^SKU\\d{3,6}$");
    
    public Sku {
        Objects.requireNonNull(value, "SKU cannot be null");
        if (!SKU_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(
                "SKU must be in format 'SKU' followed by 3-6 digits (e.g., SKU123)"
            );
        }
    }
    
    public static Sku of(String value) {
        return new Sku(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}

3. Stock.java

package com.inventory.domain.model;

public record Stock(
    int availableStock,
    int reservedStock,
    int soldStock
) {
    
    public Stock {
        if (availableStock < 0) {
            throw new IllegalArgumentException("Available stock cannot be negative");
        }
        if (reservedStock < 0) {
            throw new IllegalArgumentException("Reserved stock cannot be negative");
        }
        if (soldStock < 0) {
            throw new IllegalArgumentException("Sold stock cannot be negative");
        }
    }
    
    public static Stock empty() {
        return new Stock(0, 0, 0);
    }
    
    public static Stock withAvailable(int quantity) {
        return new Stock(quantity, 0, 0);
    }
    
    public Stock reserve(int quantity) {
        if (quantity > availableStock) {
            throw new IllegalArgumentException(
                String.format("Cannot reserve %d units. Only %d available", 
                    quantity, availableStock)
            );
        }
        return new Stock(
            availableStock - quantity,
            reservedStock + quantity,
            soldStock
        );
    }
    
    public Stock commit(int quantity) {
        if (quantity > reservedStock) {
            throw new IllegalArgumentException(
                String.format("Cannot commit %d units. Only %d reserved", 
                    quantity, reservedStock)
            );
        }
        return new Stock(
            availableStock,
            reservedStock - quantity,
            soldStock + quantity
        );
    }
    
    public Stock release(int quantity) {
        if (quantity > reservedStock) {
            throw new IllegalArgumentException(
                String.format("Cannot release %d units. Only %d reserved", 
                    quantity, reservedStock)
            );
        }
        return new Stock(
            availableStock + quantity,
            reservedStock - quantity,
            soldStock
        );
    }
    
    public int totalStock() {
        return availableStock + reservedStock;
    }
    
    public boolean hasAvailableStock(int quantity) {
        return availableStock >= quantity;
    }
}

4. ReservationId.java

package com.inventory.domain.model;

import java.util.Objects;
import java.util.UUID;

public record ReservationId(String value) {
    
    public ReservationId {
        Objects.requireNonNull(value, "ReservationId cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("ReservationId cannot be empty");
        }
    }
    
    public static ReservationId generate() {
        return new ReservationId("RES-" + UUID.randomUUID().toString());
    }
    
    public static ReservationId of(String value) {
        return new ReservationId(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}

5. ReservationStatus.java (Enum)

package com.inventory.domain.model;

public enum ReservationStatus {
    RESERVED,
    COMMITTED,
    EXPIRED,
    CANCELLED
}

CARACTERÍSTICAS:
✅ Todos são imutáveis (records)
✅ Validações no construtor compacto
✅ Factory methods (of, generate)
✅ toString() customizado
✅ Stock tem lógica de negócio

===============================================================================================================

## **📋 PROMPT 3: Domain Model - Entities (Inventory e Reservation)**
```
Crie as Entities no pacote domain/model/.

IMPORTANTE:
- Inventory é o AGGREGATE ROOT
- Use Lombok (@Data, @Builder)
- Métodos de negócio retornam resultados
- Validações dentro dos métodos

ARQUIVOS A CRIAR:

1. Inventory.java (Aggregate Root)

package com.inventory.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {
    
    private Long id;
    private StoreId storeId;
    private Sku sku;
    private String productName;
    private Stock stock;
    private LocalDateTime lastUpdated;
    
    public void reserve(int quantity) {
        this.stock = stock.reserve(quantity);
        this.lastUpdated = LocalDateTime.now();
    }
    
    public void commit(int quantity) {
        this.stock = stock.commit(quantity);
        this.lastUpdated = LocalDateTime.now();
    }
    
    public void release(int quantity) {
        this.stock = stock.release(quantity);
        this.lastUpdated = LocalDateTime.now();
    }
    
    public void addStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.stock = new Stock(
            stock.availableStock() + quantity,
            stock.reservedStock(),
            stock.soldStock()
        );
        this.lastUpdated = LocalDateTime.now();
    }
    
    public boolean hasAvailableStock(int quantity) {
        return stock.hasAvailableStock(quantity);
    }
    
    public int availableStock() {
        return stock.availableStock();
    }
    
    public int reservedStock() {
        return stock.reservedStock();
    }
    
    public int soldStock() {
        return stock.soldStock();
    }
}

2. Reservation.java (Entity)

package com.inventory.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    
    private String id;
    private StoreId storeId;
    private Sku sku;
    private int quantity;
    private String customerId;
    private ReservationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime committedAt;
    
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
    
    public boolean canBeCommitted() {
        return status == ReservationStatus.RESERVED && !isExpired();
    }
    
    public boolean canBeReleased() {
        return status == ReservationStatus.RESERVED;
    }
    
    public Reservation withStatus(ReservationStatus newStatus) {
        return Reservation.builder()
            .id(this.id)
            .storeId(this.storeId)
            .sku(this.sku)
            .quantity(this.quantity)
            .customerId(this.customerId)
            .status(newStatus)
            .createdAt(this.createdAt)
            .expiresAt(this.expiresAt)
            .committedAt(newStatus == ReservationStatus.COMMITTED 
                ? LocalDateTime.now() 
                : this.committedAt)
            .build();
    }
}

3. ReservationResult.java (Value Object para retorno)

package com.inventory.domain.model;

import com.inventory.domain.event.DomainEvent;
import java.util.List;

public record ReservationResult(
    ReservationId reservationId,
    Reservation reservation,
    List<DomainEvent> events
) {
}

CARACTERÍSTICAS:
✅ Inventory é Aggregate Root
✅ Métodos de negócio (reserve, commit, release)
✅ Validações nos métodos
✅ Immutability helpers (withStatus)
✅ Métodos de consulta

===============================================================================================================

## **📋 PROMPT 4: Domain Events**
```
Crie os Domain Events no pacote domain/event/.

IMPORTANTE:
- Todos os eventos são IMUTÁVEIS (Java Records)
- Interface base DomainEvent
- Factory methods para criação
- Inclui timestamp e eventId

ARQUIVOS A CRIAR:

1. DomainEvent.java (Interface)

package com.inventory.domain.event;

import java.time.LocalDateTime;

public interface DomainEvent {
    String eventId();
    String eventType();
    LocalDateTime timestamp();
    String aggregateId();
}

2. StockReservedEvent.java

package com.inventory.domain.event;

import com.inventory.domain.model.Sku;
import com.inventory.domain.model.StoreId;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public record StockReservedEvent(
    String eventId,
    String eventType,
    LocalDateTime timestamp,
    String aggregateId,
    String reservationId,
    StoreId storeId,
    Sku sku,
    int quantity,
    String customerId
) implements DomainEvent {
    
    public StockReservedEvent {
        Objects.requireNonNull(eventId, "eventId cannot be null");
        Objects.requireNonNull(storeId, "storeId cannot be null");
        Objects.requireNonNull(sku, "sku cannot be null");
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
    }
    
    public static StockReservedEvent create(
            String reservationId,
            StoreId storeId,
            Sku sku,
            int quantity,
            String customerId) {
        return new StockReservedEvent(
            UUID.randomUUID().toString(),
            "StockReserved",
            LocalDateTime.now(),
            sku.value(),
            reservationId,
            storeId,
            sku,
            quantity,
            customerId
        );
    }
}

3. StockCommittedEvent.java

package com.inventory.domain.event;

import com.inventory.domain.model.Sku;
import com.inventory.domain.model.StoreId;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public record StockCommittedEvent(
    String eventId,
    String eventType,
    LocalDateTime timestamp,
    String aggregateId,
    String reservationId,
    StoreId storeId,
    Sku sku,
    int quantity,
    String customerId
) implements DomainEvent {
    
    public StockCommittedEvent {
        Objects.requireNonNull(eventId, "eventId cannot be null");
        Objects.requireNonNull(reservationId, "reservationId cannot be null");
        Objects.requireNonNull(storeId, "storeId cannot be null");
        Objects.requireNonNull(sku, "sku cannot be null");
    }
    
    public static StockCommittedEvent create(
            String reservationId,
            StoreId storeId,
            Sku sku,
            int quantity,
            String customerId) {
        return new StockCommittedEvent(
            UUID.randomUUID().toString(),
            "StockCommitted",
            LocalDateTime.now(),
            sku.value(),
            reservationId,
            storeId,
            sku,
            quantity,
            customerId
        );
    }
}

4. StockReleasedEvent.java

package com.inventory.domain.event;

import com.inventory.domain.model.Sku;
import com.inventory.domain.model.StoreId;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public record StockReleasedEvent(
    String eventId,
    String eventType,
    LocalDateTime timestamp,
    String aggregateId,
    String reservationId,
    StoreId storeId,
    Sku sku,
    int quantity,
    String reason
) implements DomainEvent {
    
    public StockReleasedEvent {
        Objects.requireNonNull(eventId, "eventId cannot be null");
        Objects.requireNonNull(sku, "sku cannot be null");
    }
    
    public static StockReleasedEvent create(
            String reservationId,
            StoreId storeId,
            Sku sku,
            int quantity,
            String reason) {
        return new StockReleasedEvent(
            UUID.randomUUID().toString(),
            "StockReleased",
            LocalDateTime.now(),
            sku.value(),
            reservationId,
            storeId,
            sku,
            quantity,
            reason
        );
    }
}

5. StockAddedEvent.java

package com.inventory.domain.event;

import com.inventory.domain.model.Sku;
import com.inventory.domain.model.StoreId;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public record StockAddedEvent(
    String eventId,
    String eventType,
    LocalDateTime timestamp,
    String aggregateId,
    StoreId storeId,
    Sku sku,
    int quantity,
    String reason
) implements DomainEvent {
    
    public StockAddedEvent {
        Objects.requireNonNull(eventId, "eventId cannot be null");
        Objects.requireNonNull(sku, "sku cannot be null");
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
    }
    
    public static StockAddedEvent create(
            StoreId storeId,
            Sku sku,
            int quantity,
            String reason) {
        return new StockAddedEvent(
            UUID.randomUUID().toString(),
            "StockAdded",
            LocalDateTime.now(),
            sku.value(),
            storeId,
            sku,
            quantity,
            reason
        );
    }
}

CARACTERÍSTICAS:
✅ Todos implementam DomainEvent
✅ Imutáveis (records)
✅ Factory methods (create)
✅ Validações no construtor compacto
✅ UUID gerado automaticamente

===================================================================================================================

📋 PROMPT 5: Domain Exceptions 

Crie a hierarquia de Domain Exceptions no pacote domain/exception/.

IMPORTANTE:
- Exceções devem conter informações ricas para debugging
- Incluir código de erro e detalhes estruturados
- Base exception abstrata

ARQUIVOS A CRIAR:

1. DomainException.java (Base abstrata)

package com.inventory.domain.exception;

import java.util.Map;
import lombok.Getter;

@Getter
public abstract class DomainException extends RuntimeException {
    
    private final String code;
    private final Map<String, Object> details;
    
    protected DomainException(String code, String message, Map<String, Object> details) {
        super(message);
        this.code = code;
        this.details = details != null ? details : Map.of();
    }
    
    protected DomainException(String code, String message) {
        this(code, message, Map.of());
    }
}

2. InsufficientStockException.java

package com.inventory.domain.exception;

import com.inventory.domain.model.Sku;
import com.inventory.domain.model.StoreId;
import lombok.Getter;
import java.util.Map;

@Getter
public class InsufficientStockException extends DomainException {
    
    private final Sku sku;
    private final int requestedQuantity;
    private final int availableQuantity;
    private final StoreId storeId;
    
    public InsufficientStockException(
            Sku sku, 
            int requestedQuantity, 
            int availableQuantity,
            StoreId storeId) {
        super(
            "INSUFFICIENT_STOCK",
            String.format(
                "Insufficient stock for %s. Requested: %d, Available: %d",
                sku.value(), requestedQuantity, availableQuantity
            ),
            Map.of(
                "sku", sku.value(),
                "requested", requestedQuantity,
                "available", availableQuantity,
                "storeId", storeId.value()
            )
        );
        this.sku = sku;
        this.requestedQuantity = requestedQuantity;
        this.availableQuantity = availableQuantity;
        this.storeId = storeId;
    }
}

3. ReservationNotFoundException.java

package com.inventory.domain.exception;

import java.util.Map;

public class ReservationNotFoundException extends DomainException {
    
    public ReservationNotFoundException(String reservationId) {
        super(
            "RESERVATION_NOT_FOUND",
            String.format("Reservation not found: %s", reservationId),
            Map.of("reservationId", reservationId)
        );
    }
}

4. ReservationExpiredException.java

package com.inventory.domain.exception;

import lombok.Getter;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class ReservationExpiredException extends DomainException {
    
    private final String reservationId;
    private final LocalDateTime expiredAt;
    
    public ReservationExpiredException(
            String reservationId, 
            LocalDateTime expiredAt,
            int ttlMinutes) {
        super(
            "RESERVATION_EXPIRED",
            String.format("Reservation %s expired at %s", reservationId, expiredAt),
            Map.of(
                "reservationId", reservationId,
                "expiredAt", expiredAt.toString(),
                "ttlMinutes", ttlMinutes
            )
        );
        this.reservationId = reservationId;
        this.expiredAt = expiredAt;
    }
}

5. InvalidReservationStateException.java

package com.inventory.domain.exception;

import com.inventory.domain.model.ReservationStatus;
import java.util.Map;

public class InvalidReservationStateException extends DomainException {
    
    public InvalidReservationStateException(
            String reservationId,
            ReservationStatus currentState,
            ReservationStatus expectedState) {
        super(
            "INVALID_RESERVATION_STATE",
            String.format(
                "Invalid state for reservation %s. Current: %s, Expected: %s",
                reservationId, currentState, expectedState
            ),
            Map.of(
                "reservationId", reservationId,
                "currentState", currentState.toString(),
                "expectedState", expectedState.toString()
            )
        );
    }
}

6. ProductNotFoundException.java

package com.inventory.domain.exception;

import com.inventory.domain.model.Sku;
import com.inventory.domain.model.StoreId;
import java.util.Map;

public class ProductNotFoundException extends DomainException {
    
    public ProductNotFoundException(Sku sku, StoreId storeId) {
        super(
            "PRODUCT_NOT_FOUND",
            String.format("Product %s not found in store %s", sku.value(), storeId.value()),
            Map.of(
                "sku", sku.value(),
                "storeId", storeId.value()
            )
        );
    }
}

7. InvalidStockOperationException.java

package com.inventory.domain.exception;

import java.util.Map;

public class InvalidStockOperationException extends DomainException {
    
    public InvalidStockOperationException(String operation, String reason) {
        super(
            "INVALID_STOCK_OPERATION",
            String.format("Invalid stock operation: %s. Reason: %s", operation, reason),
            Map.of(
                "operation", operation,
                "reason", reason
            )
        );
    }
}

CARACTERÍSTICAS:
✅ Hierarquia clara com base abstrata
✅ Código de erro padronizado
✅ Detalhes estruturados (Map)
✅ Mensagens descritivas
✅ Campos específicos com @Getter

===================================================================================================================

## **📋 PROMPT 6: Domain Policies (Business Rules)**
```
Crie as Business Rules como Policies no pacote domain/policy/.

IMPORTANTE:
- Policies são stateless
- Contêm apenas regras de negócio puras
- Retornam ValidationResult
- Facilmente testáveis

ARQUIVOS A CRIAR:

1. ValidationResult.java (Value Object)

package com.inventory.domain.policy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record ValidationResult(
    boolean valid,
    List<String> errors
) {
    
    public static ValidationResult success() {
        return new ValidationResult(true, List.of());
    }
    
    public static ValidationResult failure(String... errors) {
        return new ValidationResult(false, Arrays.asList(errors));
    }
    
    public static ValidationResult failure(List<String> errors) {
        return new ValidationResult(false, new ArrayList<>(errors));
    }
    
    public boolean isValid() {
        return valid;
    }
    
    public boolean hasErrors() {
        return !valid;
    }
}

2. ReservationPolicy.java

package com.inventory.domain.policy;

import com.inventory.domain.model.Inventory;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReservationPolicy {
    
    private static final int DEFAULT_TTL_MINUTES = 15;
    private static final int MIN_QUANTITY = 1;
    private static final int MAX_QUANTITY_PER_RESERVATION = 100;
    
    public ValidationResult validate(Inventory inventory, int quantity) {
        List<String> errors = new ArrayList<>();
        
        // Valida quantidade mínima
        if (quantity < MIN_QUANTITY) {
            errors.add(String.format(
                "Quantity must be at least %d", MIN_QUANTITY
            ));
        }
        
        // Valida quantidade máxima
        if (quantity > MAX_QUANTITY_PER_RESERVATION) {
            errors.add(String.format(
                "Quantity cannot exceed %d per reservation", 
                MAX_QUANTITY_PER_RESERVATION
            ));
        }
        
        // Valida estoque disponível
        if (!inventory.hasAvailableStock(quantity)) {
            errors.add(String.format(
                "Insufficient stock. Requested: %d, Available: %d",
                quantity, inventory.availableStock()
            ));
        }
        
        return errors.isEmpty() 
            ? ValidationResult.success() 
            : ValidationResult.failure(errors);
    }
    
    public Duration getTtl() {
        return Duration.ofMinutes(DEFAULT_TTL_MINUTES);
    }
    
    public boolean canReserve(Inventory inventory, int quantity) {
        return validate(inventory, quantity).isValid();
    }
    
    public int getMaxQuantityPerReservation() {
        return MAX_QUANTITY_PER_RESERVATION;
    }
}

3. StockValidationPolicy.java

package com.inventory.domain.policy;

import com.inventory.domain.model.Stock;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class StockValidationPolicy {
    
    private static final int LOW_STOCK_THRESHOLD = 10;
    private static final int CRITICAL_STOCK_THRESHOLD = 5;
    private static final int MAX_STOCK_PER_ITEM = 10000;
    
    public ValidationResult validateStockOperation(
            Stock currentStock, 
            int quantity, 
            StockOperation operation) {
        
        List<String> errors = new ArrayList<>();
        
        switch (operation) {
            case ADD -> validateAdd(currentStock, quantity, errors);
            case RESERVE -> validateReserve(currentStock, quantity, errors);
            case COMMIT -> validateCommit(currentStock, quantity, errors);
            case RELEASE -> validateRelease(currentStock, quantity, errors);
        }
        
        return errors.isEmpty() 
            ? ValidationResult.success() 
            : ValidationResult.failure(errors);
    }
    
    private void validateAdd(Stock stock, int quantity, List<String> errors) {
        if (quantity <= 0) {
            errors.add("Quantity to add must be positive");
        }
        
        int newTotal = stock.totalStock() + quantity;
        if (newTotal > MAX_STOCK_PER_ITEM) {
            errors.add(String.format(
                "Cannot add %d units. Would exceed maximum stock of %d",
                quantity, MAX_STOCK_PER_ITEM
            ));
        }
    }
    
    private void validateReserve(Stock stock, int quantity, List<String> errors) {
        if (quantity <= 0) {
            errors.add("Quantity to reserve must be positive");
        }
        
        if (quantity > stock.availableStock()) {
            errors.add(String.format(
                "Cannot reserve %d units. Only %d available",
                quantity, stock.availableStock()
            ));
        }
    }
    
    private void validateCommit(Stock stock, int quantity, List<String> errors) {
        if (quantity > stock.reservedStock()) {
            errors.add(String.format(
                "Cannot commit %d units. Only %d reserved",
                quantity, stock.reservedStock()
            ));
        }
    }
    
    private void validateRelease(Stock stock, int quantity, List<String> errors) {
        if (quantity > stock.reservedStock()) {
            errors.add(String.format(
                "Cannot release %d units. Only %d reserved",
                quantity, stock.reservedStock()
            ));
        }
    }
    
    public boolean isLowStock(Stock stock) {
        return stock.availableStock() < LOW_STOCK_THRESHOLD;
    }
    
    public boolean isCriticalStock(Stock stock) {
        return stock.availableStock() < CRITICAL_STOCK_THRESHOLD;
    }
    
    public StockLevel checkStockLevel(Stock stock) {
        if (stock.availableStock() == 0) {
            return StockLevel.OUT_OF_STOCK;
        } else if (isCriticalStock(stock)) {
            return StockLevel.CRITICAL;
        } else if (isLowStock(stock)) {
            return StockLevel.LOW;
        } else {
            return StockLevel.NORMAL;
        }
    }
}

4. StockOperation.java (Enum)

package com.inventory.domain.policy;

public enum StockOperation {
    ADD,
    RESERVE,
    COMMIT,
    RELEASE
}

5. StockLevel.java (Enum)

package com.inventory.domain.policy;

public enum StockLevel {
    OUT_OF_STOCK,
    CRITICAL,
    LOW,
    NORMAL
}

6. ExpirationPolicy.java

package com.inventory.domain.policy;

import com.inventory.domain.model.Reservation;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ExpirationPolicy {
    
    private static final int EXPIRING_SOON_MINUTES = 5;
    
    public boolean isExpired(Reservation reservation) {
        return LocalDateTime.now().isAfter(reservation.getExpiresAt());
    }
    
    public Duration timeUntilExpiration(Reservation reservation) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = reservation.getExpiresAt();
        
        if (now.isAfter(expiresAt)) {
            return Duration.ZERO;
        }
        
        return Duration.between(now, expiresAt);
    }
    
    public boolean isExpiringSoon(Reservation reservation) {
        Duration remaining = timeUntilExpiration(reservation);
        long remainingMinutes = remaining.toMinutes();
        
        return remainingMinutes > 0 && remainingMinutes < EXPIRING_SOON_MINUTES;
    }
    
    public int getMinutesUntilExpiration(Reservation reservation) {
        return (int) timeUntilExpiration(reservation).toMinutes();
    }
}

CARACTERÍSTICAS:
✅ Policies são stateless (@Component)
✅ Apenas regras de negócio
✅ Retornam ValidationResult
✅ Constantes configuráveis
✅ Métodos auxiliares (isLowStock, etc)

==================================================================================================================

## **📋 PROMPT 7: Application Ports (Interfaces)**
```
Crie as Ports (interfaces) no pacote application/port/.

IMPORTANTE:
- Ports são INTERFACES (contratos)
- Input Ports = Use Cases
- Output Ports = Dependências externas

ARQUIVOS A CRIAR:

==== INPUT PORTS (application/port/input/) ====

1. ReserveStockCommand.java

package com.inventory.application.port.input;

import com.inventory.domain.model.Sku;
import com.inventory.domain.model.StoreId;
import java.util.Objects;

public record ReserveStockCommand(
    StoreId storeId,
    Sku sku,
    int quantity,
    String customerId
) {
    public ReserveStockCommand {
        Objects.requireNonNull(storeId, "storeId cannot be null");
        Objects.requireNonNull(sku, "sku cannot be null");
        Objects.requireNonNull(customerId, "customerId cannot be null");
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
    }
}

2. CommitStockCommand.java

package com.inventory.application.port.input;

import java.util.Objects;

public record CommitStockCommand(
    String reservationId,
    String orderId
) {
    public CommitStockCommand {
        Objects.requireNonNull(reservationId, "reservationId cannot be null");
        Objects.requireNonNull(orderId, "orderId cannot be null");
    }
}

3. ReleaseStockCommand.java

package com.inventory.application.port.input;

import java.util.Objects;

public record ReleaseStockCommand(
    String reservationId,
    String reason
) {
    public ReleaseStockCommand {
        Objects.requireNonNull(reservationId, "reservationId cannot be null");
        Objects.requireNonNull(reason, "reason cannot be null");
    }
}

4. Result.java (Generic Result type)

package com.inventory.application.port.input;

public sealed interface Result<S, F> 
    permits Result.Success, Result.Failure {
    
    record Success<S, F>(S value) implements Result<S, F> {
        public boolean isSuccess() { return true; }
        public boolean isFailure() { return false; }
        
        public S getValue() { return value; }
        public F getError() { throw new UnsupportedOperationException("Success has no error"); }
    }
    
    record Failure<S, F>(F error) implements Result<S, F> {
        public boolean isSuccess() { return false; }
        public boolean isFailure() { return true; }
        
        public S getValue() { throw new UnsupportedOperationException("Failure has no value"); }
        public F getError() { return error; }
    }
    
    static <S, F> Result<S, F> success(S value) {
        return new Success<>(value);
    }
    
    static <S, F> Result<S, F> failure(F error) {
        return new Failure<>(error);
    }
    
    boolean isSuccess();
    boolean isFailure();
    S getValue();
    F getError();
}

5. DomainError.java

package com.inventory.application.port.input;

import com.inventory.domain.exception.DomainException;
import java.util.Map;

public record DomainError(
    String code,
    String message,
    Map<String, Object> details
) {
    public static DomainError from(DomainException ex) {
        return new DomainError(
            ex.getCode(),
            ex.getMessage(),
            ex.getDetails()
        );
    }
    
    public static DomainError of(String code, String message) {
        return new DomainError(code, message, Map.of());
    }
}

6. ReserveStockUseCase.java

package com.inventory.application.port.input;

import com.inventory.domain.model.ReservationId;

public interface ReserveStockUseCase {
    Result<ReservationId, DomainError> reserve(ReserveStockCommand command);
}

7. CommitStockUseCase.java

package com.inventory.application.port.input;

public interface CommitStockUseCase {
    Result<String, DomainError> commit(CommitStockCommand command);
}

8. ReleaseStockUseCase.java

package com.inventory.application.port.input;

public interface ReleaseStockUseCase {
    Result<Void, DomainError> release(ReleaseStockCommand command);
}

9. QueryStockUseCase.java

package com.inventory.application.port.input;

import com.inventory.domain.model.Sku;
import com.inventory.domain.model.StoreId;
import java.util.Optional;

public interface QueryStockUseCase {
    Optional<InventoryView> findByStoreAndSku(StoreId storeId, Sku sku);
}

10. InventoryView.java (DTO para queries)

package com.inventory.application.port.input;

import com.inventory.domain.model.Inventory;

public record InventoryView(
    String storeId,
    String sku,
    String productName,
    int availableStock,
    int reservedStock,
    int soldStock
) {
    public static InventoryView from(Inventory inventory) {
        return new InventoryView(
            inventory.getStoreId().value(),
            inventory.getSku().value(),
            inventory.getProductName(),
            inventory.availableStock(),
            inventory.reservedStock(),
            inventory.soldStock()
        );
    }
}

==== OUTPUT PORTS (application/port/output/) ====

11. InventoryRepository.java

package com.inventory.application.port.output;

import com.inventory.domain.model.Inventory;
import com.inventory.domain.model.Sku;
import com.inventory.domain.model.StoreId;
import java.util.Optional;

public interface InventoryRepository {
    Optional<Inventory> findByStoreIdAndSku(StoreId storeId, Sku sku);
    Optional<Inventory> findByStoreIdAndSkuWithLock(StoreId storeId, Sku sku);
    Inventory save(Inventory inventory);
    boolean existsByStoreIdAndSku(StoreId storeId, Sku sku);
}

12. ReservationRepository.java

package com.inventory.application.port.output;

import com.inventory.domain.model.Reservation;
import com.inventory.domain.model.ReservationStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    Optional<Reservation> findById(String reservationId);
    Reservation save(Reservation reservation);
    List<Reservation> findByStatus(ReservationStatus status);
    List<Reservation> findExpiredReservations(LocalDateTime before);
    void delete(Reservation reservation);
}

13. EventStore.java

package com.inventory.application.port.output;

import com.inventory.domain.event.DomainEvent;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventStore {
    void store(DomainEvent event);
    List<DomainEvent> findByAggregateId(String aggregateId);
    List<DomainEvent> findByAggregateIdAndTimestamp(
        String aggregateId, 
        LocalDateTime from, 
        LocalDateTime to
    );
    Optional<DomainEvent> findByEventId(String eventId);
    List<DomainEvent> findAll();
}

14. EventPublisher.java

package com.inventory.application.port.output;

import com.inventory.domain.event.DomainEvent;
import java.util.List;

public interface EventPublisher {
    void publish(DomainEvent event);
    void publishBatch(List<DomainEvent> events);
}

CARACTERÍSTICAS:
✅ Ports são interfaces (contratos)
✅ Input Ports = Use Cases do sistema
✅ Output Ports = Abstrações de infraestrutura
✅ Commands são imutáveis (records)
✅ Result type para Railway Oriented Programming

===================================================================================================================

📋 PROMPT 8: Application Services - ReserveStockService
Crie o ReserveStockService no pacote application/service/.

IMPORTANTE:
- Implementa ReserveStockUseCase
- Orquestra domain model
- Gerencia transações
- Publica eventos
- Trata exceções

ARQUIVO A CRIAR:

ReserveStockService.java

package com.inventory.application.service;

import com.inventory.application.port.input.*;
import com.inventory.application.port.output.*;
import com.inventory.domain.event.StockReservedEvent;
import com.inventory.domain.exception.DomainException;
import com.inventory.domain.exception.ProductNotFoundException;
import com.inventory.domain.model.*;
import com.inventory.domain.policy.ReservationPolicy;
import com.inventory.domain.policy.ValidationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReserveStockService implements ReserveStockUseCase {
    
    private final InventoryRepository inventoryRepository;
    private final ReservationRepository reservationRepository;
    private final EventPublisher eventPublisher;
    private final EventStore eventStore;
    private final ReservationPolicy reservationPolicy;
    
    @Override
    public Result<ReservationId, DomainError> reserve(ReserveStockCommand command) {
        
        log.info("Reserving stock - store: {}, sku: {}, quantity: {}, customer: {}", 
            command.storeId(), command.sku(), command.quantity(), command.customerId());
        
        try {
            // 1. Load aggregate with pessimistic lock
            Inventory inventory = inventoryRepository
                .findByStoreIdAndSkuWithLock(command.storeId(), command.sku())
                .orElseThrow(() -> new ProductNotFoundException(
                    command.sku(), 
                    command.storeId()
                ));
            
            log.debug("Inventory loaded - available: {}, reserved: {}", 
                inventory.availableStock(), inventory.reservedStock());
            
            // 2. Validate business rules
            ValidationResult validation = reservationPolicy.validate(
                inventory, 
                command.quantity()
            );
            
            if (validation.hasErrors()) {
                log.warn("Validation failed: {}", validation.errors());
                return Result.failure(new DomainError(
                    "VALIDATION_ERROR",
                    "Reservation validation failed",
                    Map.of("errors", validation.errors())
                ));
            }
            
            // 3. Execute domain operation
            ReservationId reservationId = ReservationId.generate();
            inventory.reserve(command.quantity());
            
            // 4. Create reservation
            Reservation reservation = Reservation.builder()
                .id(reservationId.value())
                .storeId(command.storeId())
                .sku(command.sku())
                .quantity(command.quantity())
                .customerId(command.customerId())
                .status(ReservationStatus.RESERVED)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plus(reservationPolicy.getTtl()))
                .build();
            
            // 5. Persist changes
            inventoryRepository.save(inventory);
            reservationRepository.save(reservation);
            
            // 6. Create and publish domain event
            StockReservedEvent event = StockReservedEvent.create(
                reservationId.value(),
                command.storeId(),
                command.sku(),
                command.quantity(),
                command.customerId()
            );
            
            eventStore.store(event);
            eventPublisher.publish(event);
            
            log.info("✅ Stock reserved successfully - reservationId: {}, expiresAt: {}", 
                reservationId, reservation.getExpiresAt());
            
            return Result.success(reservationId);
            
        } catch (DomainException ex) {
            log.error("❌ Domain error during reservation: {}", ex.getMessage(), ex);
            return Result.failure(DomainError.from(ex));
            
        } catch (Exception ex) {
            log.error("❌ Unexpected error during reservation", ex);
            return Result.failure(new DomainError(
                "INTERNAL_ERROR",
                "An unexpected error occurred",
                Map.of("error", ex.getMessage())
            ));
        }
    }
}

CARACTERÍSTICAS:
✅ @Service para Spring gerenciar
✅ @Transactional para atomicidade
✅ @RequiredArgsConstructor do Lombok
✅ @Slf4j para logging estruturado
✅ Try-catch com conversão para Result
✅ Logging em cada etapa importante
✅ Pessimistic locking
✅ Event Store + Event Publisher

==================================================================================================================

## **📋 PROMPT 9: Application Services - CommitStockService**
```
Crie o CommitStockService no pacote application/service/.

ARQUIVO A CRIAR:

CommitStockService.java

package com.inventory.application.service;

import com.inventory.application.port.input.*;
import com.inventory.application.port.output.*;
import com.inventory.domain.event.StockCommittedEvent;
import com.inventory.domain.exception.*;
import com.inventory.domain.model.*;
import com.inventory.domain.policy.ExpirationPolicy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommitStockService implements CommitStockUseCase {
    
    private final InventoryRepository inventoryRepository;
    private final ReservationRepository reservationRepository;
    private final EventPublisher eventPublisher;
    private final EventStore eventStore;
    private final ExpirationPolicy expirationPolicy;
    
    @Override
    public Result<String, DomainError> commit(CommitStockCommand command) {
        
        log.info("Committing reservation: {}", command.reservationId());
        
        try {
            // 1. Load reservation
            Reservation reservation = reservationRepository
                .findById(command.reservationId())
                .orElseThrow(() -> new ReservationNotFoundException(
                    command.reservationId()
                ));
            
            log.debug("Reservation found - status: {}, quantity: {}", 
                reservation.getStatus(), reservation.getQuantity());
            
            // 2. Validate state
            if (reservation.getStatus() != ReservationStatus.RESERVED) {
                throw new InvalidReservationStateException(
                    command.reservationId(),
                    reservation.getStatus(),
                    ReservationStatus.RESERVED
                );
            }
            
            // 3. Check expiration
            if (expirationPolicy.isExpired(reservation)) {
                throw new ReservationExpiredException(
                    command.reservationId(),
                    reservation.getExpiresAt(),
                    15
                );
            }
            
            // 4. Load inventory with lock
            Inventory inventory = inventoryRepository
                .findByStoreIdAndSkuWithLock(
                    reservation.getStoreId(),
                    reservation.getSku()
                )
                .orElseThrow(() -> new ProductNotFoundException(
                    reservation.getSku(),
                    reservation.getStoreId()
                ));
            
            // 5. Execute domain operation (reserved → sold)
            inventory.commit(reservation.getQuantity());
            
            // 6. Update reservation status
            Reservation committedReservation = reservation.withStatus(
                ReservationStatus.COMMITTED
            );
            
            // 7. Persist
            inventoryRepository.save(inventory);
            reservationRepository.save(committedReservation);
            
            // 8. Create and publish event
            StockCommittedEvent event = StockCommittedEvent.create(
                command.reservationId(),
                reservation.getStoreId(),
                reservation.getSku(),
                reservation.getQuantity(),
                reservation.getCustomerId()
            );
            
            eventStore.store(event);
            eventPublisher.publish(event);
            
            log.info("✅ Reservation committed successfully - orderId: {}", 
                command.orderId());
            
            return Result.success(command.orderId());
            
        } catch (DomainException ex) {
            log.error("❌ Domain error during commit: {}", ex.getMessage(), ex);
            return Result.failure(DomainError.from(ex));
            
        } catch (Exception ex) {
            log.error("❌ Unexpected error during commit", ex);
            return Result.failure(DomainError.of(
                "INTERNAL_ERROR",
                "An unexpected error occurred"
            ));
        }
    }
}

CARACTERÍSTICAS:
✅ Valida status da reserva
✅ Verifica expiração
✅ Pessimistic lock no inventory
✅ Atualiza reservation para COMMITTED
✅ Publica StockCommittedEvent

====================================================================================================================

## **📋 PROMPT 10: Application Services - ReleaseStockService e QueryStockService**
```
Crie ReleaseStockService e QueryStockService no pacote application/service/.

ARQUIVOS A CRIAR:

1. ReleaseStockService.java

package com.inventory.application.service;

import com.inventory.application.port.input.*;
import com.inventory.application.port.output.*;
import com.inventory.domain.event.StockReleasedEvent;
import com.inventory.domain.exception.*;
import com.inventory.domain.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReleaseStockService implements ReleaseStockUseCase {
    
    private final InventoryRepository inventoryRepository;
    private final ReservationRepository reservationRepository;
    private final EventPublisher eventPublisher;
    private final EventStore eventStore;
    
    @Override
    public Result<Void, DomainError> release(ReleaseStockCommand command) {
        
        log.info("Releasing reservation: {}, reason: {}", 
            command.reservationId(), command.reason());
        
        try {
            // 1. Load reservation
            Reservation reservation = reservationRepository
                .findById(command.reservationId())
                .orElseThrow(() -> new ReservationNotFoundException(
                    command.reservationId()
                ));
            
            // 2. Validate can be released
            if (reservation.getStatus() == ReservationStatus.COMMITTED) {
                throw new InvalidReservationStateException(
                    command.reservationId(),
                    reservation.getStatus(),
                    ReservationStatus.RESERVED
                );
            }
            
            // 3. Load inventory with lock
            Inventory inventory = inventoryRepository
                .findByStoreIdAndSkuWithLock(
                    reservation.getStoreId(),
                    reservation.getSku()
                )
                .orElseThrow(() -> new ProductNotFoundException(
                    reservation.getSku(),
                    reservation.getStoreId()
                ));
            
            // 4. Execute domain operation (return to available)
            inventory.release(reservation.getQuantity());
            
            // 5. Update reservation
            Reservation releasedReservation = reservation.withStatus(
                ReservationStatus.CANCELLED
            );
            
            // 6. Persist
            inventoryRepository.save(inventory);
            reservationRepository.save(releasedReservation);
            
            // 7. Create and publish event
            StockReleasedEvent event = StockReleasedEvent.create(
                command.reservationId(),
                reservation.getStoreId(),
                reservation.getSku(),
                reservation.getQuantity(),
                command.reason()
            );
            
            eventStore.store(event);
            eventPublisher.publish(event);
            
            log.info("✅ Reservation released successfully");
            
            return Result.success(null);
            
        } catch (DomainException ex) {
            log.error("❌ Domain error during release: {}", ex.getMessage(), ex);
            return Result.failure(DomainError.from(ex));
            
        } catch (Exception ex) {
            log.error("❌ Unexpected error during release", ex);
            return Result.failure(DomainError.of(
                "INTERNAL_ERROR",
                "An unexpected error occurred"
            ));
        }
    }
}

2. QueryStockService.java

package com.inventory.application.service;

import com.inventory.application.port.input.InventoryView;
import com.inventory.application.port.input.QueryStockUseCase;
import com.inventory.application.port.output.InventoryRepository;
import com.inventory.domain.model.Sku;
import com.inventory.domain.model.StoreId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueryStockService implements QueryStockUseCase {
    
    private final InventoryRepository inventoryRepository;
    
    @Override
    @Transactional(readOnly = true)
    public Optional<InventoryView> findByStoreAndSku(StoreId storeId, Sku sku) {
        
        log.debug("Querying stock - store: {}, sku: {}", storeId, sku);
        
        return inventoryRepository
            .findByStoreIdAndSku(storeId, sku)
            .map(inventory -> {
                log.debug("Inventory found - available: {}", 
                    inventory.availableStock());
                return InventoryView.from(inventory);
            });
    }
}

CARACTERÍSTICAS:
✅ ReleaseStockService libera estoque reservado
✅ Atualiza reservation para CANCELLED
✅ Publica StockReleasedEvent
✅ QueryStockService usa @Transactional(readOnly = true)
✅ Converte Inventory → InventoryView

===================================================================================================================

## **📋 PROMPT 11: Output Adapter - Persistence (JPA Entities)**
```
Crie as JPA Entities no pacote adapters/output/persistence/entity/.

IMPORTANTE:
- Entidades JPA (anotações @Entity, @Table)
- Mapeamento de Value Objects para tipos primitivos
- Relacionamentos se necessário

ARQUIVOS A CRIAR:

1. InventoryEntity.java

package com.inventory.adapters.output.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"store_id", "sku"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "store_id", nullable = false, length = 50)
    private String storeId;
    
    @Column(name = "sku", nullable = false, length = 20)
    private String sku;
    
    @Column(name = "product_name", nullable = false, length = 200)
    private String productName;
    
    @Column(name = "available_stock", nullable = false)
    private Integer availableStock;
    
    @Column(name = "reserved_stock", nullable = false)
    private Integer reservedStock;
    
    @Column(name = "sold_stock", nullable = false)
    private Integer soldStock;
    
    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;
    
    @Version
    private Long version; // Optimistic locking support
}

2. ReservationEntity.java

package com.inventory.adapters.output.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations", indexes = {
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_expires_at", columnList = "expires_at")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationEntity {
    
    @Id
    @Column(length = 50)
    private String id;
    
    @Column(name = "store_id", nullable = false, length = 50)
    private String storeId;
    
    @Column(name = "sku", nullable = false, length = 20)
    private String sku;
    
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    
    @Column(name = "customer_id", nullable = false, length = 50)
    private String customerId;
    
    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ReservationStatusEntity status;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
    
    @Column(name = "committed_at")
    private LocalDateTime committedAt;
}

3. ReservationStatusEntity.java (Enum)

package com.inventory.adapters.output.persistence.entity;

public enum ReservationStatusEntity {
    RESERVED,
    COMMITTED,
    EXPIRED,
    CANCELLED
}

4. EventEntity.java

package com.inventory.adapters.output.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "domain_events", indexes = {
    @Index(name = "idx_aggregate_id", columnList = "aggregate_id"),
    @Index(name = "idx_event_type", columnList = "event_type"),
    @Index(name = "idx_timestamp", columnList = "timestamp")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventEntity {
    
    @Id
    @Column(length = 50)
    private String eventId;
    
    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;
    
    @Column(name = "aggregate_id", nullable = false, length = 50)
    private String aggregateId;
    
    @Column(name = "aggregate_type", nullable = false, length = 50)
    private String aggregateType;
    
    @Column(name = "payload", nullable = false, columnDefinition = "TEXT")
    private String payload;
    
    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata;
    
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
    
    @Column(name = "version", nullable = false)
    private Integer version;
}

CARACTERÍSTICAS:
✅ Anotações JPA (@Entity, @Table, @Column)
✅ Unique constraints onde necessário
✅ Indexes para queries frequentes
✅ @Version para optimistic locking
✅ Enums como STRING
✅ TEXT para JSONs (payload)

===================================================================================================================

## **📋 PROMPT 12: Output Adapter - JPA Repositories**
```
Crie os JPA Repositories no pacote adapters/output/persistence/repository/.

ARQUIVOS A CRIAR:

1. InventoryJpaRepository.java

package com.inventory.adapters.output.persistence.repository;

import com.inventory.adapters.output.persistence.entity.InventoryEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryJpaRepository extends JpaRepository<InventoryEntity, Long> {
    
    Optional<InventoryEntity> findByStoreIdAndSku(String storeId, String sku);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM InventoryEntity i WHERE i.storeId = :storeId AND i.sku = :sku")
    Optional<InventoryEntity> findByStoreIdAndSkuWithLock(
        @Param("storeId") String storeId, 
        @Param("sku") String sku
    );
    
    boolean existsByStoreIdAndSku(String storeId, String sku);
}

2. ReservationJpaRepository.java

package com.inventory.adapters.output.persistence.repository;

import com.inventory.adapters.output.persistence.entity.ReservationEntity;
import com.inventory.adapters.output.persistence.entity.ReservationStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, String> {
    
    List<ReservationEntity> findByStatus(ReservationStatusEntity status);
    
    @Query("SELECT r FROM ReservationEntity r WHERE r.status = 'RESERVED' AND r.expiresAt < :before")
    List<ReservationEntity> findExpiredReservations(@Param("before") LocalDateTime before);
}

3. EventJpaRepository.java

package com.inventory.adapters.output.persistence.repository;

import com.inventory.adapters.output.persistence.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventJpaRepository extends JpaRepository<EventEntity, String> {
    
    List<EventEntity> findByAggregateIdOrderByTimestampAsc(String aggregateId);
    
    @Query("SELECT e FROM EventEntity e WHERE e.aggregateId = :aggregateId " +
           "AND e.timestamp BETWEEN :from AND :to ORDER BY e.timestamp ASC")
    List<EventEntity> findByAggregateIdAndTimestampBetween(
        @Param("aggregateId") String aggregateId,
        @Param("from") LocalDateTime from,
        @Param("to") LocalDateTime to
    );
    
    List<EventEntity> findByEventTypeOrderByTimestampDesc(String eventType);
}

CARACTERÍSTICAS:
✅ Extends JpaRepository
✅ Query methods (findBy...)
✅ @Lock para pessimistic locking
✅ @Query customizadas
✅ Ordenação (OrderBy)

======================================================================================

PROMPT 13: Persistence Mappers
Crie os Mappers no pacote adapters/output/persistence/mapper/.

IMPORTANTE:
- Mappers convertem Domain ↔ JPA Entity
- Use MapStruct para geração automática
- Métodos para Value Objects

ARQUIVOS A CRIAR:

1. InventoryPersistenceMapper.java

package com.inventory.adapters.output.persistence.mapper;

import com.inventory.adapters.output.persistence.entity.InventoryEntity;
import com.inventory.domain.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface InventoryPersistenceMapper {
    
    @Mapping(target = "storeId", source = "storeId", qualifiedByName = "storeIdToString")
    @Mapping(target = "sku", source = "sku", qualifiedByName = "skuToString")
    @Mapping(target = "availableStock", source = "stock", qualifiedByName = "stockToAvailable")
    @Mapping(target = "reservedStock", source = "stock", qualifiedByName = "stockToReserved")
    @Mapping(target = "soldStock", source = "stock", qualifiedByName = "stockToSold")
    @Mapping(target = "version", ignore = true)
    InventoryEntity toEntity(Inventory domain);
    
    @Mapping(target = "storeId", source = "storeId", qualifiedByName = "stringToStoreId")
    @Mapping(target = "sku", source = "sku", qualifiedByName = "stringToSku")
    @Mapping(target = "stock", expression = "java(mapToStock(entity))")
    Inventory toDomain(InventoryEntity entity);
    
    // Value Object conversions
    @Named("storeIdToString")
    default String storeIdToString(StoreId storeId) {
        return storeId != null ? storeId.value() : null;
    }
    
    @Named("stringToStoreId")
    default StoreId stringToStoreId(String storeId) {
        return storeId != null ? StoreId.of(storeId) : null;
    }
    
    @Named("skuToString")
    default String skuToString(Sku sku) {
        return sku != null ? sku.value() : null;
    }
    
    @Named("stringToSku")
    default Sku stringToSku(String sku) {
        return sku != null ? Sku.of(sku) : null;
    }
    
    @Named("stockToAvailable")
    default Integer stockToAvailable(Stock stock) {
        return stock != null ? stock.availableStock() : 0;
    }
    
    @Named("stockToReserved")
    default Integer stockToReserved(Stock stock) {
        return stock != null ? stock.reservedStock() : 0;
    }
    
    @Named("stockToSold")
    default Integer stockToSold(Stock stock) {
        return stock != null ? stock.soldStock() : 0;
    }
    
    default Stock mapToStock(InventoryEntity entity) {
        return new Stock(
            entity.getAvailableStock(),
            entity.getReservedStock(),
            entity.getSoldStock()
        );
    }
}

2. ReservationPersistenceMapper.java

package com.inventory.adapters.output.persistence.mapper;

import com.inventory.adapters.output.persistence.entity.ReservationEntity;
import com.inventory.adapters.output.persistence.entity.ReservationStatusEntity;
import com.inventory.domain.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ReservationPersistenceMapper {
    
    @Mapping(target = "storeId", source = "storeId", qualifiedByName = "storeIdToString")
    @Mapping(target = "sku", source = "sku", qualifiedByName = "skuToString")
    @Mapping(target = "status", source = "status", qualifiedByName = "statusToEntity")
    ReservationEntity toEntity(Reservation domain);
    
    @Mapping(target = "storeId", source = "storeId", qualifiedByName = "stringToStoreId")
    @Mapping(target = "sku", source = "sku", qualifiedByName = "stringToSku")
    @Mapping(target = "status", source = "status", qualifiedByName = "entityToStatus")
    Reservation toDomain(ReservationEntity entity);
    
    @Named("storeIdToString")
    default String storeIdToString(StoreId storeId) {
        return storeId != null ? storeId.value() : null;
    }
    
    @Named("stringToStoreId")
    default StoreId stringToStoreId(String storeId) {
        return storeId != null ? StoreId.of(storeId) : null;
    }
    
    @Named("skuToString")
    default String skuToString(Sku sku) {
        return sku != null ? sku.value() : null;
    }
    
    @Named("stringToSku")
    default Sku stringToSku(String sku) {
        return sku != null ? Sku.of(sku) : null;
    }
    
    @Named("statusToEntity")
    default ReservationStatusEntity statusToEntity(ReservationStatus status) {
        return status != null ? ReservationStatusEntity.valueOf(status.name()) : null;
    }
    
    @Named("entityToStatus")
    default ReservationStatus entityToStatus(ReservationStatusEntity status) {
        return status != null ? ReservationStatus.valueOf(status.name()) : null;
    }
}

3. EventPersistenceMapper.java

package com.inventory.adapters.output.persistence.mapper;

import com.inventory.adapters.output.persistence.entity.EventEntity;
import com.inventory.domain.event.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventPersistenceMapper {
    
    private final ObjectMapper objectMapper;
    
    public EventEntity toEntity(DomainEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            
            return EventEntity.builder()
                .eventId(event.eventId())
                .eventType(event.eventType())
                .aggregateId(event.aggregateId())
                .aggregateType("Inventory")
                .payload(payload)
                .timestamp(event.timestamp())
                .version(1)
                .build();
                
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize event", e);
        }
    }
    
    public DomainEvent toDomain(EventEntity entity) {
        try {
            // Simple deserialization based on event type
            return switch (entity.getEventType()) {
                case "StockReserved" -> 
                    objectMapper.readValue(entity.getPayload(), StockReservedEvent.class);
                case "StockCommitted" -> 
                    objectMapper.readValue(entity.getPayload(), StockCommittedEvent.class);
                case "StockReleased" -> 
                    objectMapper.readValue(entity.getPayload(), StockReleasedEvent.class);
                case "StockAdded" -> 
                    objectMapper.readValue(entity.getPayload(), StockAddedEvent.class);
                default -> 
                    throw new IllegalArgumentException("Unknown event type: " + entity.getEventType());
            };
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize event", e);
        }
    }
}

CARACTERÍSTICAS:
✅ MapStruct para conversão automática
✅ @Named para conversões customizadas
✅ Value Objects ↔ Strings
✅ Enums ↔ Entity Enums
✅ ObjectMapper para eventos (JSON)

========================================================================================

## **📋 PROMPT 14: Repository Adapters (Implementação)**
```
Crie os Adapters que implementam os Output Ports no pacote adapters/output/persistence/adapter/.

ARQUIVOS A CRIAR:

1. InventoryJpaAdapter.java

package com.inventory.adapters.output.persistence.adapter;

import com.inventory.adapters.output.persistence.mapper.InventoryPersistenceMapper;
import com.inventory.adapters.output.persistence.repository.InventoryJpaRepository;
import com.inventory.application.port.output.InventoryRepository;
import com.inventory.domain.model.Inventory;
import com.inventory.domain.model.Sku;
import com.inventory.domain.model.StoreId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryJpaAdapter implements InventoryRepository {
    
    private final InventoryJpaRepository jpaRepository;
    private final InventoryPersistenceMapper mapper;
    
    @Override
    public Optional<Inventory> findByStoreIdAndSku(StoreId storeId, Sku sku) {
        log.debug("Finding inventory - store: {}, sku: {}", storeId, sku);
        
        return jpaRepository
            .findByStoreIdAndSku(storeId.value(), sku.value())
            .map(mapper::toDomain);
    }
    
    @Override
    public Optional<Inventory> findByStoreIdAndSkuWithLock(StoreId storeId, Sku sku) {
        log.debug("Finding inventory with lock - store: {}, sku: {}", storeId, sku);
        
        return jpaRepository
            .findByStoreIdAndSkuWithLock(storeId.value(), sku.value())
            .map(mapper::toDomain);
    }
    
    @Override
    public Inventory save(Inventory inventory) {
        log.debug("Saving inventory - sku: {}, available: {}", 
            inventory.getSku(), inventory.availableStock());
        
        var entity = mapper.toEntity(inventory);
        var saved = jpaRepository.save(entity);
        
        return mapper.toDomain(saved);
    }
    
    @Override
    public boolean existsByStoreIdAndSku(StoreId storeId, Sku sku) {
        return jpaRepository.existsByStoreIdAndSku(storeId.value(), sku.value());
    }
}

2. ReservationJpaAdapter.java

package com.inventory.adapters.output.persistence.adapter;

import com.inventory.adapters.output.persistence.entity.ReservationStatusEntity;
import com.inventory.adapters.output.persistence.mapper.ReservationPersistenceMapper;
import com.inventory.adapters.output.persistence.repository.ReservationJpaRepository;
import com.inventory.application.port.output.ReservationRepository;
import com.inventory.domain.model.Reservation;
import com.inventory.domain.model.ReservationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationJpaAdapter implements ReservationRepository {
    
    private final ReservationJpaRepository jpaRepository;
    private final ReservationPersistenceMapper mapper;
    
    @Override
    public Optional<Reservation> findById(String reservationId) {
        log.debug("Finding reservation: {}", reservationId);
        
        return jpaRepository
            .findById(reservationId)
            .map(mapper::toDomain);
    }
    
    @Override
    public Reservation save(Reservation reservation) {
        log.debug("Saving reservation: {}, status: {}", 
            reservation.getId(), reservation.getStatus());
        
        var entity = mapper.toEntity(reservation);
        var saved = jpaRepository.save(entity);
        
        return mapper.toDomain(saved);
    }
    
    @Override
    public List<Reservation> findByStatus(ReservationStatus status) {
        var entityStatus = ReservationStatusEntity.valueOf(status.name());
        
        return jpaRepository
            .findByStatus(entityStatus)
            .stream()
            .map(mapper::toDomain)
            .toList();
    }
    
    @Override
    public List<Reservation> findExpiredReservations(LocalDateTime before) {
        return jpaRepository
            .findExpiredReservations(before)
            .stream()
            .map(mapper::toDomain)
            .toList();
    }
    
    @Override
    public void delete(Reservation reservation) {
        log.debug("Deleting reservation: {}", reservation.getId());
        jpaRepository.deleteById(reservation.getId());
    }
}

3. EventStoreJpaAdapter.java

package com.inventory.adapters.output.persistence.adapter;

import com.inventory.adapters.output.persistence.mapper.EventPersistenceMapper;
import com.inventory.adapters.output.persistence.repository.EventJpaRepository;
import com.inventory.application.port.output.EventStore;
import com.inventory.domain.event.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventStoreJpaAdapter implements EventStore {
    
    private final EventJpaRepository jpaRepository;
    private final EventPersistenceMapper mapper;
    
    @Override
    public void store(DomainEvent event) {
        log.debug("Storing event: {} ({})", event.eventId(), event.eventType());
        
        var entity = mapper.toEntity(event);
        jpaRepository.save(entity);
        
        log.info("Event stored: {}", event.eventId());
    }
    
    @Override
    public List<DomainEvent> findByAggregateId(String aggregateId) {
        log.debug("Finding events by aggregateId: {}", aggregateId);
        
        return jpaRepository
            .findByAggregateIdOrderByTimestampAsc(aggregateId)
            .stream()
            .map(mapper::toDomain)
            .toList();
    }
    
    @Override
    public List<DomainEvent> findByAggregateIdAndTimestamp(
            String aggregateId, 
            LocalDateTime from, 
            LocalDateTime to) {
        
        log.debug("Finding events by aggregateId: {} between {} and {}", 
            aggregateId, from, to);
        
        return jpaRepository
            .findByAggregateIdAndTimestampBetween(aggregateId, from, to)
            .stream()
            .map(mapper::toDomain)
            .toList();
    }
    
    @Override
    public Optional<DomainEvent> findByEventId(String eventId) {
        return jpaRepository
            .findById(eventId)
            .map(mapper::toDomain);
    }
    
    @Override
    public List<DomainEvent> findAll() {
        return jpaRepository
            .findAll()
            .stream()
            .map(mapper::toDomain)
            .toList();
    }
}

CARACTERÍSTICAS:
✅ Implementam Output Ports (interfaces)
✅ Delegam para JPA Repositories
✅ Usam Mappers para conversão
✅ Logging estruturado
✅ @Component para Spring gerenciar

========================================================================================

## **📋 PROMPT 15: Event Publisher Adapter (Opcional - Stub)**
```
Crie o Event Publisher Adapter no pacote adapters/output/messaging/.

IMPORTANTE:
- Este é um STUB (implementação simples)
- Apenas loga os eventos
- Em produção seria SNS/SQS real

ARQUIVO A CRIAR:

InMemoryEventPublisher.java

package com.inventory.adapters.output.messaging;

import com.inventory.application.port.output.EventPublisher;
import com.inventory.domain.event.DomainEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InMemoryEventPublisher implements EventPublisher {
    
    private final ObjectMapper objectMapper;
    
    @Override
    public void publish(DomainEvent event) {
        try {
            String eventJson = objectMapper.writeValueAsString(event);
            
            log.info("📤 EVENT PUBLISHED: {} ({})", 
                event.eventType(), event.eventId());
            log.debug("Event payload: {}", eventJson);
            
            // TODO: Integrar com SNS/SQS quando disponível
            // snsClient.publish(...)
            
        } catch (Exception e) {
            log.error("Failed to publish event: {}", event.eventId(), e);
            // Em produção, considerar DLQ ou retry
        }
    }
    
    @Override
    public void publishBatch(List<DomainEvent> events) {
        log.info("📤 Publishing batch of {} events", events.size());
        events.forEach(this::publish);
    }
}

CARACTERÍSTICAS:
✅ Implementação simples (stub)
✅ Loga eventos publicados
✅ Serializa para JSON
✅ TODO comentado para integração futura
✅ Fácil trocar por implementação SNS real

=======================================================================================

## **📋 PROMPT 15: Event Publisher Adapter (Opcional - Stub)**
```
Crie o Event Publisher Adapter no pacote adapters/output/messaging/.

IMPORTANTE:
- Este é um STUB (implementação simples)
- Apenas loga os eventos
- Em produção seria SNS/SQS real

ARQUIVO A CRIAR:

InMemoryEventPublisher.java

package com.inventory.adapters.output.messaging;

import com.inventory.application.port.output.EventPublisher;
import com.inventory.domain.event.DomainEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InMemoryEventPublisher implements EventPublisher {
    
    private final ObjectMapper objectMapper;
    
    @Override
    public void publish(DomainEvent event) {
        try {
            String eventJson = objectMapper.writeValueAsString(event);
            
            log.info("📤 EVENT PUBLISHED: {} ({})", 
                event.eventType(), event.eventId());
            log.debug("Event payload: {}", eventJson);
            
            // TODO: Integrar com SNS/SQS quando disponível
            // snsClient.publish(...)
            
        } catch (Exception e) {
            log.error("Failed to publish event: {}", event.eventId(), e);
            // Em produção, considerar DLQ ou retry
        }
    }
    
    @Override
    public void publishBatch(List<DomainEvent> events) {
        log.info("📤 Publishing batch of {} events", events.size());
        events.forEach(this::publish);
    }
}

CARACTERÍSTICAS:
✅ Implementação simples (stub)
✅ Loga eventos publicados
✅ Serializa para JSON
✅ TODO comentado para integração futura
✅ Fácil trocar por implementação SNS real

NOTA: Para integrar com LocalStack/SNS:
1. Adicionar SnsClient como dependência
2. Publicar no tópico SNS
3. Configurar endpoint LocalStack

====================================================================================

## **📋 PROMPT 16: Input Adapters - REST DTOs**
```
Crie os DTOs REST no pacote adapters/input/rest/dto/.

ARQUIVOS A CRIAR:

1. ReserveStockRequest.java

package com.inventory.adapters.input.rest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ReserveStockRequest(
    
    @NotBlank(message = "storeId is required")
    String storeId,
    
    @NotBlank(message = "sku is required")
    @Pattern(regexp = "^SKU\\d{3,6}$", message = "SKU must be in format SKUxxx")
    String sku,
    
    @Min(value = 1, message = "quantity must be at least 1")
    int quantity,
    
    @NotBlank(message = "customerId is required")
    String customerId
) {
}

2. CommitStockRequest.java

package com.inventory.adapters.input.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record CommitStockRequest(
    
    @NotBlank(message = "reservationId is required")
    String reservationId,
    
    @NotBlank(message = "orderId is required")
    String orderId
) {
}

3. ReleaseStockRequest.java

package com.inventory.adapters.input.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record ReleaseStockRequest(
    
    @NotBlank(message = "reservationId is required")
    String reservationId,
    
    @NotBlank(message = "reason is required")
    String reason
) {
}

4. ReservationResponse.java

package com.inventory.adapters.input.rest.dto;

import java.time.LocalDateTime;

public record ReservationResponse(
    String reservationId,
    String storeId,
    String sku,
    int quantity,
    String status,
    LocalDateTime expiresAt,
    String message
) {
}

5. InventoryResponse.java

package com.inventory.adapters.input.rest.dto;

public record InventoryResponse(
    String storeId,
    String sku,
    String productName,
    int availableStock,
    int reservedStock,
    int soldStock,
    int totalStock
) {
}

6. ErrorResponse.java

package com.inventory.adapters.input.rest.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record ErrorResponse(
    LocalDateTime timestamp,
    int status,
    String error,
    String message,
    String path,
    Map<String, Object> details,
    List<FieldErrorDetail> fieldErrors
) {
    
    public record FieldErrorDetail(
        String field,
        String message,
        Object rejectedValue
    ) {
    }
    
    public static ErrorResponse of(
            int status,
            String error,
            String message,
            String path) {
        return new ErrorResponse(
            LocalDateTime.now(),
            status,
            error,
            message,
            path,
            Map.of(),
            List.of()
        );
    }
    
    public static ErrorResponse of(
            int status,
            String error,
            String message,
            String path,
            Map<String, Object> details) {
        return new ErrorResponse(
            LocalDateTime.now(),
            status,
            error,
            message,
            path,
            details,
            List.of()
        );
    }
}

CARACTERÍSTICAS:
✅ Java Records (imutáveis)
✅ Bean Validation (@NotBlank, @Min, @Pattern)
✅ Mensagens de erro customizadas
✅ Factory methods (ErrorResponse.of)
✅ Nested records (FieldErrorDetail)

========================================================================================

PROMPT 17: REST Mappers (DTO ↔ Command)

Crie os REST Mappers no pacote adapters/input/rest/mapper/.

ARQUIVOS A CRIAR:

1. InventoryRestMapper.java

package com.inventory.adapters.input.rest.mapper;

import com.inventory.adapters.input.rest.dto.*;
import com.inventory.application.port.input.*;
import com.inventory.domain.model.Sku;
import com.inventory.domain.model.StoreId;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class InventoryRestMapper {
    
    public ReserveStockCommand toCommand(ReserveStockRequest request) {
        return new ReserveStockCommand(
            StoreId.of(request.storeId()),
            Sku.of(request.sku()),
            request.quantity(),
            request.customerId()
        );
    }
    
    public CommitStockCommand toCommand(CommitStockRequest request) {
        return new CommitStockCommand(
            request.reservationId(),
            request.orderId()
        );
    }
    
    public ReleaseStockCommand toCommand(ReleaseStockRequest request) {
        return new ReleaseStockCommand(
            request.reservationId(),
            request.reason()
        );
    }
    
    public ReservationResponse toReservationResponse(
            String reservationId,
            ReserveStockCommand command,
            LocalDateTime expiresAt) {
        return new ReservationResponse(
            reservationId,
            command.storeId().value(),
            command.sku().value(),
            command.quantity(),
            "RESERVED",
            expiresAt,
            "Stock reserved successfully. Complete your purchase before expiration."
        );
    }
    
    public ReservationResponse toCommitResponse(String orderId) {
        return new ReservationResponse(
            null,
            null,
            null,
            0,
            "COMMITTED",
            null,
            "Stock committed successfully. OrderId: " + orderId
        );
    }
    
    public InventoryResponse toInventoryResponse(InventoryView view) {
        return new InventoryResponse(
            view.storeId(),
            view.sku(),
            view.productName(),
            view.availableStock(),
            view.reservedStock(),
            view.soldStock(),
            view.availableStock() + view.reservedStock()
        );
    }
}

CARACTERÍSTICAS:
✅ Converte DTOs → Commands
✅ Converte Domain → Response DTOs
✅ @Component para Spring injetar
✅ Métodos específicos para cada caso
```

========================================================================================

## **📋 PROMPT 18: REST Controllers**
```
Crie os REST Controllers no pacote adapters/input/rest/controller/.

ARQUIVOS A CRIAR:

1. InventoryCommandController.java

package com.inventory.adapters.input.rest.controller;

import com.inventory.adapters.input.rest.dto.*;
import com.inventory.adapters.input.rest.mapper.InventoryRestMapper;
import com.inventory.application.port.input.*;
import com.inventory.domain.model.ReservationId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Inventory Commands", description = "Write operations for inventory management")
public class InventoryCommandController {
    
    private final ReserveStockUseCase reserveStockUseCase;
    private final CommitStockUseCase commitStockUseCase;
    private final ReleaseStockUseCase releaseStockUseCase;
    private final InventoryRestMapper mapper;
    
    @PostMapping("/reserve")
    @Operation(summary = "Reserve stock", description = "Reserve stock for a customer with TTL")
    public ResponseEntity<?> reserveStock(@Valid @RequestBody ReserveStockRequest request) {
        
        log.info("📥 Reserve stock request - store: {}, sku: {}, qty: {}", 
            request.storeId(), request.sku(), request.quantity());
        
        var command = mapper.toCommand(request);
        var result = reserveStockUseCase.reserve(command);
        
        if (result.isSuccess()) {
            ReservationId reservationId = result.getValue();
            LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(15);
            
            var response = mapper.toReservationResponse(
                reservationId.value(),
                command,
                expiresAt
            );
            
            log.info("✅ Stock reserved - reservationId: {}", reservationId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            log.warn("❌ Reservation failed: {}", result.getError().message());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(
                    HttpStatus.BAD_REQUEST.value(),
                    result.getError().code(),
                    result.getError().message(),
                    "/api/v1/inventory/reserve",
                    result.getError().details()
                ));
        }
    }
    
    @PostMapping("/commit")
    @Operation(summary = "Commit reservation", description = "Confirm sale and commit reserved stock")
    public ResponseEntity<?> commitStock(@Valid @RequestBody CommitStockRequest request) {
        
        log.info("📥 Commit stock request - reservationId: {}", request.reservationId());
        
        var command = mapper.toCommand(request);
        var result = commitStockUseCase.commit(command);
        
        if (result.isSuccess()) {
            String orderId = result.getValue();
            var response = mapper.toCommitResponse(orderId);
            
            log.info("✅ Stock committed - orderId: {}", orderId);
            return ResponseEntity.ok(response);
        } else {
            log.warn("❌ Commit failed: {}", result.getError().message());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(
                    HttpStatus.BAD_REQUEST.value(),
                    result.getError().code(),
                    result.getError().message(),
                    "/api/v1/inventory/commit",
                    result.getError().details()
                ));
        }
    }
    
    @PostMapping("/release")
    @Operation(summary = "Release reservation", description = "Cancel reservation and return stock")
    public ResponseEntity<?> releaseStock(@Valid @RequestBody ReleaseStockRequest request) {
        
        log.info("📥 Release stock request - reservationId: {}", request.reservationId());
        
        var command = mapper.toCommand(request);
        var result = releaseStockUseCase.release(command);
        
        if (result.isSuccess()) {
            log.info("✅ Stock released");
            return ResponseEntity.ok(new ReservationResponse(
                request.reservationId(),
                null, null, 0,
                "CANCELLED",
                null,
                "Reservation cancelled successfully"
            ));
        } else {
            log.warn("❌ Release failed: {}", result.getError().message());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(
                    HttpStatus.BAD_REQUEST.value(),
                    result.getError().code(),
                    result.getError().message(),
                    "/api/v1/inventory/release",
                    result.getError().details()
                ));
        }
    }
}

2. InventoryQueryController.java

package com.inventory.adapters.input.rest.controller;

import com.inventory.adapters.input.rest.dto.ErrorResponse;
import com.inventory.adapters.input.rest.dto.InventoryResponse;
import com.inventory.adapters.input.rest.mapper.InventoryRestMapper;
import com.inventory.application.port.input.QueryStockUseCase;
import com.inventory.domain.model.Sku;
import com.inventory.domain.model.StoreId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Inventory Queries", description = "Read operations for inventory management")
public class InventoryQueryController {
    
    private final QueryStockUseCase queryStockUseCase;
    private final InventoryRestMapper mapper;
    
    @GetMapping("/{storeId}/{sku}")
    @Operation(summary = "Get stock information", description = "Query current stock levels")
    public ResponseEntity<?> getStock(
            @PathVariable String storeId,
            @PathVariable String sku) {
        
        log.info("📥 Query stock - store: {}, sku: {}", storeId, sku);
        
        try {
            var result = queryStockUseCase.findByStoreAndSku(
                StoreId.of(storeId),
                Sku.of(sku)
            );
            
            if (result.isPresent()) {
                var response = mapper.toInventoryResponse(result.get());
                log.debug("✅ Stock found - available: {}", response.availableStock());
                return ResponseEntity.ok(response);
            } else {
                log.warn("❌ Stock not found");
                return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse.of(
                        HttpStatus.NOT_FOUND.value(),
                        "PRODUCT_NOT_FOUND",
                        String.format("Product %s not found in store %s", sku, storeId),
                        String.format("/api/v1/inventory/%s/%s", storeId, sku)
                    ));
            }
            
        } catch (IllegalArgumentException ex) {
            log.error("❌ Invalid input: {}", ex.getMessage());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(
                    HttpStatus.BAD_REQUEST.value(),
                    "INVALID_INPUT",
                    ex.getMessage(),
                    String.format("/api/v1/inventory/%s/%s", storeId, sku)
                ));
        }
    }
    
    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check if service is running")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Inventory Service is running");
    }
}

CARACTERÍSTICAS:
✅ @RestController + @RequestMapping
✅ Validação com @Valid
✅ OpenAPI documentation (@Operation, @Tag)
✅ Logging estruturado
✅ Railway Oriented Programming (Result)
✅ HTTP Status codes apropriados
✅ Separação Command/Query (CQRS)
```

========================================================================================

## **📋 PROMPT 19: Global Exception Handler**
```
Crie o Global Exception Handler no pacote adapters/input/rest/exception/.

ARQUIVO A CRIAR:

GlobalExceptionHandler.java

package com.inventory.adapters.input.rest.exception;

import com.inventory.adapters.input.rest.dto.ErrorResponse;
import com.inventory.adapters.input.rest.dto.ErrorResponse.FieldErrorDetail;
import com.inventory.domain.exception.DomainException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(
            DomainException ex, 
            HttpServletRequest request) {
        
        log.error("Domain exception: {} - {}", ex.getCode(), ex.getMessage(), ex);
        
        ErrorResponse response = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            ex.getCode(),
            ex.getMessage(),
            request.getRequestURI(),
            ex.getDetails(),
            List.of()
        );
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        
        log.error("Validation failed: {}", ex.getMessage());
        
        List<FieldErrorDetail> fieldErrors = ex.getBindingResult()
            .getAllErrors()
            .stream()
            .map(error -> {
                String fieldName = ((FieldError) error).getField();
                String message = error.getDefaultMessage();
                Object rejectedValue = ((FieldError) error).getRejectedValue();
                return new FieldErrorDetail(fieldName, message, rejectedValue);
            })
            .toList();
        
        ErrorResponse response = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "VALIDATION_ERROR",
            "Request validation failed",
            request.getRequestURI(),
            Map.of("errorCount", fieldErrors.size()),
            fieldErrors
        );
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex,
            HttpServletRequest request) {
        
        log.error("Illegal argument: {}", ex.getMessage());
        
        ErrorResponse response = ErrorResponse.of(
            HttpStatus.BAD_REQUEST.value(),
            "INVALID_ARGUMENT",
            ex.getMessage(),
            request.getRequestURI()
        );
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {
        
        log.error("Unexpected error", ex);
        
        ErrorResponse response = ErrorResponse.of(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "INTERNAL_ERROR",
            "An unexpected error occurred. Please try again later.",
            request.getRequestURI(),
            Map.of("type", ex.getClass().getSimpleName())
        );
        
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(response);
    }
}

CARACTERÍSTICAS:
✅ @RestControllerAdvice para captura global
✅ Handlers específicos por tipo de exceção
✅ DomainException → 400 com detalhes
✅ Validation → 400 com field errors
✅ Generic → 500 sem expor detalhes internos
✅ Logging de todas as exceções
✅ ErrorResponse padronizado
```

========================================================================================

## **📋 PROMPT 20: Configurações - Swagger e CORS**
```
Crie as configurações no pacote config/.

ARQUIVOS A CRIAR:

1. OpenApiConfig.java

package com.inventory.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI inventoryServiceAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Inventory Management Service")
                .description("""
                    Event-Driven Inventory Management System with Event Sourcing and CQRS.
                    
                    **Architecture Patterns:**
                    - Hexagonal Architecture (Ports & Adapters)
                    - Domain-Driven Design (DDD)
                    - Event Sourcing
                    - CQRS (Command Query Responsibility Segregation)
                    - Pessimistic Locking for consistency
                    
                    **Key Features:**
                    - Reserve stock with TTL (15 minutes)
                    - Commit reservations (finalize sale)
                    - Release reservations (cancel)
                    - Query stock levels
                    - Complete audit trail via Event Store
                    """)
                .version("1.0.0")
                .contact(new Contact()
                    .name("Inventory Team")
                    .email("inventory@example.com"))
                .license(new License()
                    .name("MIT")
                    .url("https://opensource.org/licenses/MIT")))
            .servers(List.of(
                new Server()
                    .url("http://localhost:8081")
                    .description("Local development server")
            ));
    }
}

2. WebConfig.java

package com.inventory.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins("http://localhost:3000", "http://localhost:4200")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3600);
    }
}

3. JacksonConfig.java

package com.inventory.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}

CARACTERÍSTICAS:
✅ Swagger UI configurado
✅ Documentação completa da API
✅ CORS habilitado para development
✅ ObjectMapper configurado para LocalDateTime
```

========================================================================================

## **📋 PROMPT 21: Data Initialization**
```
Crie o script de inicialização no pacote config/.

ARQUIVO A CRIAR:

DataInitializer.java

package com.inventory.config;

import com.inventory.adapters.output.persistence.entity.InventoryEntity;
import com.inventory.adapters.output.persistence.repository.InventoryJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final InventoryJpaRepository inventoryRepository;
    
    @Override
    public void run(String... args) {
        log.info("🚀 Initializing database with sample data...");
        
        if (inventoryRepository.count() > 0) {
            log.info("Database already contains data. Skipping initialization.");
            return;
        }
        
        List<InventoryEntity> initialInventory = List.of(
            createInventory("STORE-01", "SKU123", "Notebook Dell XPS 13", 100),
            createInventory("STORE-01", "SKU456", "iPhone 15 Pro", 50),
            createInventory("STORE-01", "SKU789", "Samsung Galaxy S24", 75),
            createInventory("STORE-02", "SKU123", "Notebook Dell XPS 13", 80),
            createInventory("STORE-02", "SKU456", "iPhone 15 Pro", 40),
            createInventory("STORE-03", "SKU789", "Samsung Galaxy S24", 60)
        );
        
        inventoryRepository.saveAll(initialInventory);
        
        log.info("✅ Database initialized with {} products", initialInventory.size());
        log.info("Sample SKUs: SKU123, SKU456, SKU789");
        log.info("Sample Stores: STORE-01, STORE-02, STORE-03");
    }
    
    private InventoryEntity createInventory(
            String storeId, 
            String sku, 
            String productName, 
            int stock) {
        return InventoryEntity.builder()
            .storeId(storeId)
            .sku(sku)
            .productName(productName)
            .availableStock(stock)
            .reservedStock(0)
            .soldStock(0)
            .lastUpdated(LocalDateTime.now())
            .build();
    }
}

CARACTERÍSTICAS:
✅ CommandLineRunner para executar no startup
✅ Verifica se já existe dados
✅ Cria inventory inicial para testes
✅ Logging informativo

========================================================================================
