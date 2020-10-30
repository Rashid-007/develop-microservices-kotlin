package com.shop.itemservice

import com.fasterxml.jackson.databind.SerializationFeature
import com.shop.itemservice.config.Health
import com.shop.itemservice.config.HealthStatus
import com.shop.itemservice.config.simpleJwt
import com.shop.itemservice.web.api.v1.routeApiV1
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.response.*
import io.ktor.routing.*

/**
 *
fun main(args: Array<String>) {
    embeddedServer(
    Netty,
    watchPaths = listOf("solutions/exercise4"),
    port = 8080,
    module = Application::mymodule
    ).apply { start(wait = true) }
}
 */
fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module() {
    val client = HttpClient(Apache) {
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    install(Authentication) {
        jwt {
            verifier(simpleJwt.verifier)
            validate {
                UserIdPrincipal(it.payload.getClaim("name").asString())
            }
        }
    }

    routing {
        get("/") {
            call.respondText("Welcome to shop!", contentType = ContentType.Text.Plain)
        }

        get("/health") {
            call.response.status(HttpStatusCode.OK)
            call.respond(Health(HealthStatus.HEALTHY))
        }
        // route to main api
        routeApiV1("api/v1")
    }
}