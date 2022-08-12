package com.sivalabs.devzone;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ArchTest {

    JavaClasses importedClasses =
            new ClassFileImporter()
                    .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                    .importPackages("com.sivalabs.devzone");

    @Test
    void domainShouldNotDependOnWebLayer() {
        noClasses()
                .that()
                .resideInAnyPackage("com.sivalabs.devzone.domain..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage("com.sivalabs.devzone.web..", "com.sivalabs.devzone.api..")
                .because("Domain layer should not depend on Web or API layer")
                .check(importedClasses);
    }

    @Test
    void shouldNotUseFieldInjection() {
        noFields().should().beAnnotatedWith(Autowired.class).check(importedClasses);
    }

    @Test
    void shouldFollowLayeredArchitecture() {
        layeredArchitecture()
                .layer("Config")
                .definedBy("..config..")
                .layer("Web")
                .definedBy("..web..", "..api..")
                .layer("Service")
                .definedBy("..services..")
                .layer("Mapper")
                .definedBy("..mappers..")
                .layer("Persistence")
                .definedBy("..repositories..")
                .whereLayer("Web")
                .mayNotBeAccessedByAnyLayer()
                .whereLayer("Service")
                .mayOnlyBeAccessedByLayers("Config", "Web", "Mapper")
                .whereLayer("Persistence")
                .mayOnlyBeAccessedByLayers("Service")
                .check(importedClasses);
    }

    @Test
    void shouldFollowNamingConvention() {
        classes()
                .that()
                .resideInAPackage("com.sivalabs.devzone.domain.repositories")
                .should()
                .haveSimpleNameEndingWith("Repository")
                .check(importedClasses);

        classes()
                .that()
                .resideInAPackage("com.sivalabs.devzone.domain.services")
                .should()
                .haveSimpleNameEndingWith("Service")
                .check(importedClasses);
    }

    @Test
    void shouldNotUseJunit4Classes() {
        JavaClasses classes = new ClassFileImporter().importPackages("com.sivalabs.devzone");

        noClasses()
                .should()
                .accessClassesThat()
                .resideInAnyPackage("org.junit")
                .because("Tests should use Junit5 instead of Junit4")
                .check(classes);

        noMethods()
                .should()
                .beAnnotatedWith("org.junit.Test")
                .orShould()
                .beAnnotatedWith("org.junit.Ignore")
                .because("Tests should use Junit5 instead of Junit4")
                .check(classes);
    }
}
