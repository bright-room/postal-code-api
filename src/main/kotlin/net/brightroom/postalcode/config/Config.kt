package net.brightroom.postalcode.config

import io.ktor.server.application.Application
import net.brightroom.postalcode.config.plugin.configureDatabase
import net.brightroom.postalcode.config.plugin.configureDependencyInjection
import net.brightroom.postalcode.config.plugin.configureLogging
import net.brightroom.postalcode.config.plugin.configureRouting

fun Application.module() {
    configureRouting()
    configureLogging()
    configureDatabase()
    configureDependencyInjection()
}
