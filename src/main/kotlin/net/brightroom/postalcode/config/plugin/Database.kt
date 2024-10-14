package net.brightroom.postalcode.config.plugin

import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig

fun Application.configureDatabase() {
    val config = environment.config

    val url = config.property("ktor.datasource.url").getString()
    val username = config.property("ktor.datasource.username").getString()
    val password = config.property("ktor.datasource.password").getString()

    val maximumPoolSize = config.property("ktor.datasource.hikari.maximumPoolSize").getString().toInt()
    val isAutoCommit = config.property("ktor.datasource.hikari.isAutoCommit").getString().toBoolean()
    val transactionIsolation = config.property("ktor.datasource.hikari.transactionIsolation").getString()

    val datasource = HikariDataSource().apply {
        this.jdbcUrl = url
        this.username = username
        this.password = password
        this.maximumPoolSize = maximumPoolSize
        this.isAutoCommit = isAutoCommit
        this.transactionIsolation = transactionIsolation
    }

    Database.connect(
        datasource,
        databaseConfig = DatabaseConfig.invoke { useNestedTransactions = true },
    )
}
