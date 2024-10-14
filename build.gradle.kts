plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.spotless)
}

group = "net.brightroom"
version = "0.0.1"

dependencies {
    implementation(libs.bundles.ktor.server)
    implementation(libs.bundles.ktor.server.datasource.access)

    testImplementation(kotlin("test-junit5"))
    testImplementation(libs.bundles.ktor.server.test)
}

java {
    toolchain {
        val javaVersion = libs.versions.java.get()
        languageVersion = JavaLanguageVersion.of(javaVersion)
    }
}

application {
    mainClass.set("net.brightroom.postalcode.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
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
