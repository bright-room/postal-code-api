plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.spotless)
    alias(libs.plugins.ksp)
}

group = "net.brightroom"
version = "0.0.1"

dependencies {
    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(project.dependencies.platform(libs.koin.annotations.bom))

    implementation(libs.bundles.ktor.server)
    implementation(libs.bundles.ktor.server.datasource.access)

    runtimeOnly(libs.postgresql)
    ksp(libs.koin.ksp.compiler)

    testImplementation(kotlin("test-junit5"))
    testImplementation(libs.bundles.ktor.server.test)
}

sourceSets {
    main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
}

java {
    toolchain {
        val javaVersion = libs.versions.java.get()
        languageVersion = JavaLanguageVersion.of(javaVersion)
    }
}

application {
    mainClass.set("net.brightroom.postalcode.ApplicationKt")
}

ksp {
    arg("KOIN_CONFIG_CHECK", "true")
}

spotless {
    kotlin {
        targetExclude("build/**")

        val config =
            mapOf(
                "ktlint_standard_property-name" to "disabled",
                "ktlint_standard_enum-entry-name-case" to "disabled",
                "ktlint_standard_function-naming" to "disabled",
            )

        ktlint()
            .editorConfigOverride(config)
    }
    kotlinGradle {
        ktlint()
    }
}

tasks {
    test {
        useJUnitPlatform()
    }
}
