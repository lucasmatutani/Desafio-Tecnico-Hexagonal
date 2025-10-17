# 📑 Índice do Projeto - Inventory Service

## 📖 Guia de Navegação

Este documento serve como índice para toda a documentação e recursos do projeto.

---

## 📚 Documentação

### 1. [README.md](README.md) - **COMECE AQUI**
- 📋 Visão geral do projeto
- 🏗️ Explicação da Arquitetura Hexagonal
- 🚀 Tecnologias utilizadas
- ⚙️ Configurações principais
- 📊 Monitoramento e métricas

### 2. [ARCHITECTURE.md](ARCHITECTURE.md) - **GUIA DE DESENVOLVIMENTO**
- 📐 Estrutura detalhada de cada camada
- 💻 Exemplos de código completos
- ✅ Boas práticas
- 🧪 Estratégias de teste
- 📚 Referências

### 3. [QUICK_START.md](QUICK_START.md) - **INÍCIO RÁPIDO**
- 🚀 Como executar a aplicação
- 🔗 URLs e acessos importantes
- 📋 Templates de código
- 🎯 Padrões de nomenclatura
- 🐛 Troubleshooting

### 4. [STRUCTURE.txt](STRUCTURE.txt) - **VISUALIZAÇÃO**
- 📦 Árvore de diretórios
- 🎯 Fluxo de dependências
- 🛠️ Comandos úteis

---

## 🗂️ Estrutura de Pastas

### Domain Layer - Regras de Negócio
```
src/main/java/com/inventory/domain/
├── model/         → Entidades de domínio (Product, Inventory)
├── event/         → Eventos de domínio (ProductCreatedEvent)
├── policy/        → Políticas de negócio (StockReservationPolicy)
├── service/       → Serviços de domínio (PricingService)
└── exception/     → Exceções customizadas (ProductNotFoundException)
```

### Application Layer - Casos de Uso
```
src/main/java/com/inventory/application/
├── port/
│   ├── input/     → Interfaces de casos de uso (CreateProductUseCase)
│   └── output/    → Interfaces de saída (ProductRepository)
└── service/       → Implementação dos casos de uso (CreateProductService)
```

### Adapters Layer - Integração Externa
```
src/main/java/com/inventory/adapters/
├── input/
│   └── rest/              → Controllers REST (ProductController)
└── output/
    ├── persistence/       → Implementação JPA (ProductRepositoryAdapter)
    └── messaging/         → Event Publishers (SnsEventPublisher)
```

---

## 🔧 Arquivos de Configuração

### [pom.xml](pom.xml)
Configuração Maven com todas as dependências:
- Spring Boot 3.4.0
- Java 21
- Lombok, MapStruct
- AWS SDK, Resilience4j
- OpenAPI, Prometheus

### [application.yml](src/main/resources/application.yml)
Configurações do Spring Boot:
- Datasource (H2)
- JPA/Hibernate
- Server (porta 8081)
- Logging
- AWS
- Resilience4j

### [.gitignore](.gitignore)
Arquivos a serem ignorados pelo Git

---

## 🛠️ Scripts e Ferramentas

### [verify-structure.sh](verify-structure.sh)
Script para verificar se a estrutura hexagonal está completa
```bash
./verify-structure.sh
```

---

## 🚀 Comandos Principais

### Desenvolvimento
```bash
# Compilar
mvn clean compile

# Executar
mvn spring-boot:run

# Testar
mvn test

# Empacotar
mvn clean package
```

### Verificação
```bash
# Verificar estrutura
./verify-structure.sh

# Ver dependências
mvn dependency:tree

# Ver plugins
mvn help:effective-pom
```

---

## 🌐 URLs Importantes

| Serviço | URL | Descrição |
|---------|-----|-----------|
| **Aplicação** | http://localhost:8081 | API REST principal |
| **H2 Console** | http://localhost:8081/h2-console | Console do banco H2 |
| **Swagger** | http://localhost:8081/swagger-ui.html | Documentação interativa da API |
| **Health** | http://localhost:8081/actuator/health | Status da aplicação |
| **Metrics** | http://localhost:8081/actuator/metrics | Métricas da aplicação |
| **Prometheus** | http://localhost:8081/actuator/prometheus | Endpoint Prometheus |

---

## 📝 Fluxo de Trabalho Recomendado

### Para Criar uma Nova Funcionalidade:

1. **Domain** - Crie as entidades e regras de negócio
   - `domain/model/` - Entidade
   - `domain/event/` - Evento (se necessário)
   - `domain/exception/` - Exceções (se necessário)

2. **Application** - Defina os casos de uso
   - `application/port/input/` - Interface do Use Case
   - `application/port/output/` - Interface do Repository
   - `application/service/` - Implementação do Use Case

3. **Adapters** - Conecte com o mundo externo
   - `adapters/input/rest/` - Controller REST
   - `adapters/output/persistence/` - Repository Adapter
   - `adapters/output/messaging/` - Event Publisher (se necessário)

4. **Testes** - Escreva testes para cada camada
   - Testes unitários para domain
   - Testes de integração para adapters
   - Testes de use case para application

---

## 🎯 Exemplos Rápidos

### Criar uma Nova Entidade
📖 Veja: [ARCHITECTURE.md - Domain Layer](ARCHITECTURE.md#1-domain-domínio---srcmainjavacomventorydomain)

### Criar um Novo Endpoint REST
📖 Veja: [ARCHITECTURE.md - Adapters Layer](ARCHITECTURE.md#3-adapters-adaptadores---srcmainjavacomventoryadapters)

### Criar um Novo Caso de Uso
📖 Veja: [ARCHITECTURE.md - Application Layer](ARCHITECTURE.md#2-application-aplicação---srcmainjavacomventoryapplication)

### Templates de Código
📖 Veja: [QUICK_START.md - Templates](QUICK_START.md#-template-de-implementação)

---

## 🧪 Testes

### Estrutura de Testes Recomendada
```
src/test/java/com/inventory/
├── domain/
│   ├── model/
│   └── service/
├── application/
│   └── service/
└── adapters/
    ├── input/rest/
    └── output/persistence/
```

📖 Veja: [ARCHITECTURE.md - Testabilidade](ARCHITECTURE.md#-testabilidade)

---

## 📊 Arquitetura Hexagonal

### Princípios Fundamentais

1. **Independência de Frameworks**
   - Domain não depende de Spring, JPA, etc.

2. **Testabilidade**
   - Cada camada pode ser testada isoladamente

3. **Independência de UI**
   - Fácil mudar de REST para GraphQL, gRPC, etc.

4. **Independência de Banco de Dados**
   - Fácil mudar de H2 para PostgreSQL, MongoDB, etc.

5. **Independência de Agentes Externos**
   - Fácil mudar de SNS para Kafka, RabbitMQ, etc.

### Fluxo de Dependências
```
Adapters → Application → Domain
```

**Regra de Ouro**: Dependências sempre apontam para dentro!

---

## 🔗 Links Úteis

### Documentação Oficial
- [Spring Boot 3.4.0](https://docs.spring.io/spring-boot/docs/3.4.0/reference/html/)
- [Java 21](https://docs.oracle.com/en/java/javase/21/)
- [Maven](https://maven.apache.org/guides/)

### Arquitetura
- [Hexagonal Architecture - Alistair Cockburn](https://alistair.cockburn.us/hexagonal-architecture/)
- [Clean Architecture - Robert Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Domain-Driven Design](https://www.domainlanguage.com/ddd/)

### Ferramentas
- [Lombok](https://projectlombok.org/)
- [MapStruct](https://mapstruct.org/)
- [Resilience4j](https://resilience4j.readme.io/)
- [SpringDoc OpenAPI](https://springdoc.org/)

---

## 💡 Dicas

1. **Sempre comece pelo Domain** - É o coração da aplicação
2. **Use Value Objects** - Para tipos como SKU, Money, Email
3. **Publique Eventos de Domínio** - Para comunicação assíncrona
4. **Mantenha Controllers Magros** - Apenas mapeamento de dados
5. **Use MapStruct** - Para conversões entre camadas
6. **Escreva Testes** - Começando pelo domain
7. **Use ArchUnit** - Para garantir as regras da arquitetura

---

## 🆘 Suporte

### Problemas Comuns
📖 Veja: [QUICK_START.md - Troubleshooting](QUICK_START.md#-troubleshooting)

### Dúvidas sobre Arquitetura
📖 Veja: [ARCHITECTURE.md](ARCHITECTURE.md)

### Início Rápido
📖 Veja: [QUICK_START.md](QUICK_START.md)

---

## 📅 Versionamento

- **Versão do Projeto**: 1.0.0
- **Spring Boot**: 3.4.0
- **Java**: 21
- **Última Atualização**: 2025-10-17

---

## ✅ Checklist de Implementação

### Configuração Inicial
- [x] Estrutura de pacotes criada
- [x] pom.xml configurado
- [x] application.yml configurado
- [x] Classe principal criada
- [x] Documentação criada

### Próximos Passos
- [ ] Implementar entidades de domínio
- [ ] Criar eventos de domínio
- [ ] Definir casos de uso
- [ ] Implementar serviços
- [ ] Criar controllers REST
- [ ] Implementar repositórios
- [ ] Configurar mensageria
- [ ] Escrever testes
- [ ] Configurar CI/CD

---

**🎯 Tudo pronto para começar o desenvolvimento!**

