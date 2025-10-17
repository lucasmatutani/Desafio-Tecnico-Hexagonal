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

