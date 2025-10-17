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
