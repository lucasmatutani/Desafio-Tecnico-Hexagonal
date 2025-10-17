# Inventory Service - Sistema de Gerenciamento de Inventário

Sistema de gerenciamento de inventário desenvolvido com Spring Boot 3.4.0 e Java 21, seguindo os princípios da **Arquitetura Hexagonal (Ports & Adapters)**.

## 🏗️ Arquitetura Hexagonal

Este projeto segue a Arquitetura Hexagonal, também conhecida como Ports & Adapters, que promove:

- **Independência de frameworks**: A lógica de negócio não depende de frameworks externos
- **Testabilidade**: Facilita testes isolados de cada camada
- **Flexibilidade**: Permite trocar adaptadores sem afetar o domínio
- **Manutenibilidade**: Código organizado e com responsabilidades bem definidas

### 📁 Estrutura de Pastas

```
src/main/java/com/inventory/
├── adapters/                    # Camada de Adaptadores
│   ├── input/                   # Adaptadores de Entrada
│   │   └── rest/               # Controllers REST
│   └── output/                  # Adaptadores de Saída
│       ├── persistence/        # Repositórios JPA
│       └── messaging/          # Publishers/Consumers de Mensageria
│
├── application/                 # Camada de Aplicação
│   ├── port/                    # Interfaces (Portas)
│   │   ├── input/              # Use Cases (Portas de Entrada)
│   │   └── output/             # Interfaces de Saída
│   └── service/                # Implementação dos Use Cases
│
└── domain/                      # Camada de Domínio (Núcleo)
    ├── model/                  # Entidades de Domínio
    ├── event/                  # Eventos de Domínio
    ├── policy/                 # Políticas de Negócio
    ├── service/                # Serviços de Domínio
    └── exception/              # Exceções de Domínio
```

## 🎯 Camadas e Responsabilidades

### 1. Domain (Domínio) - Núcleo da Aplicação
- **model/**: Entidades de negócio puras (Product, Inventory, etc.)
- **event/**: Eventos de domínio (ProductCreatedEvent, StockUpdatedEvent)
- **policy/**: Regras de negócio complexas (StockValidationPolicy)
- **service/**: Lógica de domínio que não pertence a uma entidade
- **exception/**: Exceções específicas do domínio

### 2. Application (Aplicação) - Orquestração
- **port/input/**: Interfaces de casos de uso (CreateProductUseCase, UpdateStockUseCase)
- **port/output/**: Interfaces para saída (ProductRepository, EventPublisher)
- **service/**: Implementação dos casos de uso, orquestra domínio e portas

### 3. Adapters (Adaptadores) - Integração Externa
- **input/rest/**: Controllers REST que recebem requisições HTTP
- **output/persistence/**: Implementação JPA dos repositórios
- **output/messaging/**: Implementação de publicação/consumo de eventos

## 🚀 Tecnologias

- **Java 21**: Última versão LTS do Java
- **Spring Boot 3.4.0**: Framework principal
- **Maven**: Gerenciamento de dependências
- **H2 Database**: Banco de dados em memória para desenvolvimento
- **Lombok**: Redução de boilerplate
- **MapStruct**: Mapeamento de objetos
- **AWS SDK**: Integração com SQS e SNS
- **Resilience4j**: Circuit Breaker, Retry, Rate Limiter
- **Spring Actuator**: Métricas e health checks
- **Prometheus**: Monitoramento
- **Swagger/OpenAPI**: Documentação da API
- **ArchUnit**: Testes de arquitetura

## ⚙️ Configuração

### Pré-requisitos
- Java 21
- Maven 3.8+

### Executar a aplicação

```bash
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8081`

### Acessar o H2 Console
- URL: `http://localhost:8081/h2-console`
- JDBC URL: `jdbc:h2:mem:inventory`
- Username: `sa`
- Password: (vazio)

### Documentação da API (Swagger)
- URL: `http://localhost:8081/swagger-ui.html`

### Métricas e Health Check
- Health: `http://localhost:8081/actuator/health`
- Metrics: `http://localhost:8081/actuator/metrics`
- Prometheus: `http://localhost:8081/actuator/prometheus`

## 🧪 Testes

```bash
# Executar todos os testes
mvn test

# Executar testes com coverage
mvn test jacoco:report
```

## 📊 Monitoramento

O projeto está configurado com:
- **Circuit Breaker**: Proteção contra falhas em serviços externos
- **Retry**: Tentativas automáticas em caso de falha
- **Rate Limiter**: Controle de taxa de requisições
- **Métricas Prometheus**: Monitoramento de métricas da aplicação

## 🔧 Configurações Importantes

### Resilience4j
- Circuit Breaker configurado para event publisher
- Retry configurado para operações de banco de dados
- Rate Limiter configurado para a API (100 req/s)

### AWS
- Endpoint local: `http://localhost:4566` (LocalStack)
- SNS Topic: `inventory-events`
- SQS Queue: `inventory-events`

## 📝 Logs

Níveis de log configurados:
- **ROOT**: INFO
- **com.inventory**: DEBUG
- **Hibernate SQL**: DEBUG

Formato: `%d{yyyy-MM-dd HH:mm:ss} - %msg%n`

## 🏛️ Princípios da Arquitetura

### Fluxo de Dependências
```
Adapters de Entrada → Application → Domain ← Application ← Adapters de Saída
```

### Regras
1. O **Domain** não depende de nada
2. A **Application** depende apenas do **Domain**
3. Os **Adapters** dependem da **Application** e do **Domain**
4. Dependências sempre apontam para dentro (Domain é o centro)

## 📚 Próximos Passos

1. Implementar entidades de domínio (Product, Inventory)
2. Criar casos de uso (CreateProduct, UpdateStock, etc.)
3. Implementar controllers REST
4. Implementar repositórios JPA
5. Configurar mensageria com AWS SQS/SNS
6. Adicionar testes unitários e de integração
7. Implementar testes de arquitetura com ArchUnit

## 🤝 Contribuindo

Este projeto segue as melhores práticas de:
- Clean Code
- SOLID Principles
- Domain-Driven Design (DDD)
- Hexagonal Architecture

---

**Versão**: 1.0.0  
**Spring Boot**: 3.4.0  
**Java**: 21

