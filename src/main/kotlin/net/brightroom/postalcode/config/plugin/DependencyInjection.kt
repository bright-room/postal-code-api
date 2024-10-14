package net.brightroom.postalcode.config.plugin

import io.ktor.server.application.Application
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.ksp.generated.module
import org.koin.ktor.plugin.koin
import org.koin.logger.slf4jLogger

@ComponentScan("net.brightroom.postalcode")
@Module
object DependencyInjectionModules {
    @Single
    fun test(): String = "test"
}

fun Application.configureDependencyInjection() {
    koin {
        slf4jLogger()
        modules(DependencyInjectionModules.module)
    }
}
