package net.brightroom.postalcode.config.plugin

import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.doublereceive.DoubleReceive
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.plugins.requestvalidation.RequestValidationException
import io.ktor.server.plugins.requestvalidation.ValidationResult
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.ApplicationRequest
import io.ktor.server.request.path
import io.ktor.server.resources.Resources
import io.ktor.server.response.respond
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import net.brightroom.postalcode.domain.policy.problem.ResourceNotFoundException
import net.brightroom.postalcode.presentation.api.Request

@OptIn(ExperimentalSerializationApi::class)
fun Application.configureRouting() {
    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                isLenient = true
                namingStrategy = JsonNamingStrategy.SnakeCase
            },
        )
    }

    install(DoubleReceive)
    install(Resources)

    install(RequestValidation) {
        validate<Request> { request ->
            val violations = request.validate()

            if (!violations.isValid) return@validate ValidationResult.Valid

            val errorMessage = violations
                .violations()
                .joinToString(",") { violation -> violation.message() }

            ValidationResult.Invalid(errorMessage)
        }
    }

    install(StatusPages) {
        fun creteResponse(
            statusCode: HttpStatusCode,
            request: ApplicationRequest,
            exception: Throwable,
        ): ErrorResponse {
            val title = statusCode.description
            val details = exception.message
            val instance = request.path()

            return ErrorResponse(
                title = title,
                details = details,
                instance = instance,
            )
        }

        exception<IllegalArgumentException> { call, cause ->
            val statusCode = HttpStatusCode.BadRequest
            val response = creteResponse(statusCode, call.request, cause)

            call.respond(statusCode, response)
        }

        exception<RequestValidationException> { call, cause ->
            val statusCode = HttpStatusCode.BadRequest
            val response = creteResponse(statusCode, call.request, cause)

            call.respond(statusCode, response)
        }

        exception<ResourceNotFoundException> { call, cause ->
            val statusCode = HttpStatusCode.NotFound
            val response = creteResponse(statusCode, call.request, cause)

            call.respond(statusCode, response)
        }

        exception<Exception> { call, cause ->
            val statusCode = HttpStatusCode.InternalServerError
            val response = creteResponse(statusCode, call.request, cause)

            call.respond(statusCode, response)
        }
    }
}

@Serializable
private data class ErrorResponse(
    private val type: String = "about:blank",
    private val title: String,
    private val details: String?,
    private val instance: String,
)
