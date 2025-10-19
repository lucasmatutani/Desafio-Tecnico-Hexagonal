package com.inventory.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

class HexagonalArchitectureTest {
    
    private static JavaClasses classes;
    
    @BeforeAll
    static void setup() {
        classes = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.inventory");
    }
    
    @Test
    void domainShouldNotDependOnAnyOtherLayer() {
        ArchRule rule = noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..application..", "..adapters..");
        
        rule.check(classes);
    }
    
    @Test
    void applicationShouldOnlyDependOnDomain() {
        ArchRule rule = noClasses()
            .that().resideInAPackage("..application..")
            .should().dependOnClassesThat()
                .resideInAPackage("..adapters..");
        
        rule.check(classes);
    }
    
    @Test
    void adaptersShouldNotDependOnOtherAdapters() {
        ArchRule rule = noClasses()
            .that().resideInAPackage("..adapters.input..")
            .should().dependOnClassesThat()
                .resideInAPackage("..adapters.output..");
        
        rule.check(classes);
    }
    
    @Test
    void layeredArchitectureShouldBeRespected() {
        layeredArchitecture()
            .consideringOnlyDependenciesInLayers()
            .layer("Domain").definedBy("..domain..")
            .layer("Application").definedBy("..application..")
            .layer("Adapters").definedBy("..adapters..")
            
            .whereLayer("Domain").mayNotAccessAnyLayer()
            .whereLayer("Application").mayOnlyAccessLayers("Domain")
            .whereLayer("Adapters").mayOnlyAccessLayers("Application", "Domain")
            
            .check(classes);
    }
    
    @Test
    void domainModelsShouldNotHaveJpaAnnotations() {
        ArchRule rule = noClasses()
            .that().resideInAPackage("..domain.model..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("jakarta.persistence..", "javax.persistence..");
        
        rule.check(classes);
    }
    
    @Test
    void domainModelsShouldNotHaveSpringAnnotations() {
        ArchRule rule = noClasses()
            .that().resideInAPackage("..domain.model..")
            .or().resideInAPackage("..domain.event..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("org.springframework..");
        
        rule.check(classes);
    }
    
    @Test
    void domainPoliciesCanHaveComponentAnnotation() {
        // Pragmatic approach: Policy classes (not enums/records) can have @Component for DI
        ArchRule rule = classes()
            .that().resideInAPackage("..domain.policy..")
            .and().areNotEnums()
            .and().areNotRecords()
            .should().beAnnotatedWith(org.springframework.stereotype.Component.class);
        
        rule.check(classes);
    }
    
    @Test
    void useCasePortsShouldBeInterfaces() {
        ArchRule rule = classes()
            .that().resideInAPackage("..application.port..")
            .and().haveSimpleNameEndingWith("UseCase")
            .should().beInterfaces();
        
        rule.check(classes);
    }
    
    @Test
    void outputPortsShouldBeInterfaces() {
        ArchRule rule = classes()
            .that().resideInAPackage("..application.port.output..")
            .should().beInterfaces();
        
        rule.check(classes);
    }
    
    @Test
    void identifierValueObjectsShouldBeRecords() {
        ArchRule rule = classes()
            .that().resideInAPackage("..domain.model..")
            .and().haveSimpleNameEndingWith("Id")
            .should().beRecords();
        
        rule.check(classes);
    }
    
    @Test
    void skuShouldBeRecord() {
        ArchRule rule = classes()
            .that().resideInAPackage("..domain.model..")
            .and().haveSimpleName("Sku")
            .should().beRecords();
        
        rule.check(classes);
    }
    
    @Test
    void stockShouldBeRecord() {
        ArchRule rule = classes()
            .that().resideInAPackage("..domain.model..")
            .and().haveSimpleName("Stock")
            .should().beRecords();
        
        rule.check(classes);
    }
    
    @Test
    void servicesShouldBeAnnotatedWithService() {
        ArchRule rule = classes()
            .that().resideInAPackage("..application.service..")
            .should().beAnnotatedWith(org.springframework.stereotype.Service.class);
        
        rule.check(classes);
    }
    
    @Test
    void controllersShouldBeAnnotatedWithRestController() {
        ArchRule rule = classes()
            .that().resideInAPackage("..adapters.input.rest.controller..")
            .should().beAnnotatedWith(org.springframework.web.bind.annotation.RestController.class);
        
        rule.check(classes);
    }
    
    @Test
    void repositoriesShouldBeInterfaces() {
        ArchRule rule = classes()
            .that().haveSimpleNameEndingWith("Repository")
            .and().resideInAPackage("..application.port.output..")
            .should().beInterfaces();
        
        rule.check(classes);
    }
    
    @Test
    void jpaRepositoriesShouldBeInPersistencePackage() {
        ArchRule rule = classes()
            .that().haveSimpleNameContaining("Jpa")
            .and().haveSimpleNameEndingWith("Repository")
            .should().resideInAPackage("..adapters.output.persistence.repository..");
        
        rule.check(classes);
    }
    
    @Test
    void entitiesShouldBeInDomainOrPersistence() {
        ArchRule rule = classes()
            .that().areAnnotatedWith(jakarta.persistence.Entity.class)
            .should().resideInAPackage("..adapters.output.persistence.entity..");
        
        rule.check(classes);
    }
    
    @Test
    void requestDtosShouldBeRecords() {
        ArchRule rule = classes()
            .that().resideInAPackage("..adapters.input.rest.dto..")
            .and().haveSimpleNameEndingWith("Request")
            .should().beRecords();
        
        rule.check(classes);
    }
    
    @Test
    void responseDtosShouldBeRecords() {
        ArchRule rule = classes()
            .that().resideInAPackage("..adapters.input.rest.dto..")
            .and().haveSimpleNameEndingWith("Response")
            .should().beRecords();
        
        rule.check(classes);
    }
}

