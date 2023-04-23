package com.sivalabs.devzone;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

class ArchTest {

    JavaClasses importedClasses =
            new ClassFileImporter()
                    .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                    .importPackages("com.sivalabs.devzone");

    @ParameterizedTest
    @CsvSource({"posts", "users"})
    void domainShouldNotDependOnOtherPackages(String module) {
        noClasses()
                .that()
                .resideInAnyPackage("com.sivalabs.devzone." + module + ".domain..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage(
                        "com.sivalabs.devzone." + module + ".application..",
                        "com.sivalabs.devzone." + module + ".adapter..",
                        "com.sivalabs.devzone." + module + ".web..")
                .because("Domain classes should not depend on usecases or adapter layer")
                .check(importedClasses);
    }

    @ParameterizedTest
    @CsvSource({"posts", "users"})
    void usecasesShouldNotDependOnAdapterPackage(String module) {
        noClasses()
                .that()
                .resideInAnyPackage("com.sivalabs.devzone." + module + ".application.usecases..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage("com.sivalabs.devzone." + module + ".application.adapter..")
                .because("UseCases should not depend on adapter package")
                .check(importedClasses);
    }

    @Test
    void shouldNotUseFieldInjection() {
        noFields().should().beAnnotatedWith(Autowired.class).check(importedClasses);
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
