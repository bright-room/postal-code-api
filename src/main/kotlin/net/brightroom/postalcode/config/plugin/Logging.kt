package net.brightroom.postalcode.config.plugin

import io.ktor.http.HttpHeaders
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.plugins.callid.callIdMdc
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.origin
import io.ktor.server.request.header
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import io.ktor.server.response.header
import org.slf4j.event.Level
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun Application.configureLogging() {
    val config = environment.config
    val environment = config.property("ktor.environment").getString()

    install(CallId) {
        retrieve { call -> call.request.header(HttpHeaders.XRequestId) }
        generate { Uuid.random().toString() }
        verify { callId -> callId.isNotEmpty() }
        reply { call, callId -> call.response.header(HttpHeaders.XRequestId, callId) }
    }

    install(CallLogging) {
        callIdMdc("call-id")

        level = if (environment == "dev") Level.DEBUG else Level.INFO
        filter { call ->
            call.request.path().startsWith("/v1")
        }

        format { call ->
            val remoteHost = call.request.origin.remoteHost
            val requestUrl = call.request.path()

            val status = call.response.status()
            val httpMethod = call.request.httpMethod.value
            "remoteHost: $remoteHost requestURL: $requestUrl status: $status, HTTP method: $httpMethod"
        }
    }
}
