package com.shop.userservice

import com.fasterxml.jackson.databind.SerializationFeature
import com.shop.userservice.config.*
import io.ktor.application.*
import io.ktor.auth.Authentication
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.jwt.jwt
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import com.shop.userservice.web.api.v1.routeApiV1
import com.typesafe.config.ConfigFactory
import io.ktor.auth.oauth
import io.ktor.config.HoconApplicationConfig
import io.ktor.sessions.SessionTransportTransformerMessageAuthentication
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import io.ktor.util.hex

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
fun Application.module(testing: Boolean = false) {
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

        oauth("google-oauth") {
            this.client = client
            providerLookup = { googleOauthProvider }
            urlProvider = { redirectUrl("/api/v1/login")}
        }
    }
    //install(WebSockets)
    install(Sessions) {
        cookie<JustSellSession>("oauthSampleSessionId") {
            val secretSignKey = hex("0001020304bbbb090a0b0c0d0e0f") // @TODO: Remember to change this!
            transform(SessionTransportTransformerMessageAuthentication(secretSignKey))
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