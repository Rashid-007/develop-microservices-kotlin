package com.shop.itemservice.web.api.v1

import com.fasterxml.jackson.databind.SerializationFeature
import com.shop.itemservice.config.simpleJwt
import com.shop.itemservice.domain.LoginRegister
import com.shop.itemservice.domain.User
import com.shop.itemservice.domain.users
import com.shop.itemservice.repository.shopItems
import com.shop.itemservice.web.api.v1.exception.InvalidCredentialException
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

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

    get("/item/{item_id}") {
        val itemId = call.parameters["item_id"]?.toInt() ?: error("wrong id")
        val item = shopItems[itemId] ?: error("No Such Item")
        call.respond(item)
    }

    authenticate {
    }
}