package com.shop.userservice.web.api.v1

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.readValue
import com.shop.userservice.config.JustSellSession
import com.shop.userservice.config.itemServiceBaseUrl
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.get
import io.ktor.routing.put
import io.ktor.routing.route
import com.shop.userservice.config.simpleJwt
import com.shop.userservice.domain.*
import com.shop.userservice.web.api.v1.dto.PurchaseListDto
import com.shop.userservice.web.api.v1.exception.InvalidCredentialException
import io.ktor.auth.OAuthAccessTokenResponse
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.response.respondRedirect
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import org.slf4j.LoggerFactory

fun Route.routeApiV1(path: String) = route(path) {
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
        /**
         * or
         * gson { setDateFormat(DateFormat.LONG)
                    setPrettyPrinting() }
         */
    }

    install(StatusPages) {
        // catch IllegalStateException and send back HTTP code 400
        exception<IllegalStateException> { cause ->
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to (cause.message ?: "")))
        }

        exception<InvalidCredentialException> {
            call.respond(HttpStatusCode.Unauthorized, mapOf("Ok" to false, "error" to (it.message ?: "")))
        }
    }

    post("/login") {
        val userDto = call.receive<LoginRegister>()
        val user = users.getOrPut(userDto.username) {
            User(
                userDto.username,
                userDto.password
            )
        }
        if (user.password != userDto.password) throw InvalidCredentialException("Invalid credentials")
        call.respond(mapOf("token" to simpleJwt.sign(user.username)))
    }

    authenticate {
        post("/profile") {
            val profile = call.receive<Profile>()
            profiles[profile.id] = profile
            call.respond(profiles[profile.id] ?: error("Profile not created"))
        }

        get("/profile/{profile_id}") {
            val profile = profiles.get(call.parameters["profile_id"]) ?: error("No Such Profile")
            call.respond(profile)
        }

        put("/profile/{profile_id}") {
            val purchaseListDto = call.receive<PurchaseListDto>()
            val profileId = call.parameters["profile_id"]
            profiles[profileId]?.purchasedItemsId = purchaseListDto.list
            call.respond(profiles[profileId] ?: error("profile cannot be updated"))
        }
    }
    authenticate("google-oauth") {
        route("/google/login") {
            handle {
                val principal = call.authentication.principal<OAuthAccessTokenResponse.OAuth2>()
                    ?: error("No principal")

                val json = HttpClient(Apache).get<String>("https://www.googleapis.com/userinfo/v2/me") {
                    header("Authorization", "Bearer ${principal.accessToken}")
                }

                val data = ObjectMapper().readValue<Map<String, Any?>>(json)
                val id = data["id"] as String?

                if (id != null) {
                    call.sessions.set(JustSellSession(id))
                }
                call.respondRedirect("/")
            }
        }
    }
}