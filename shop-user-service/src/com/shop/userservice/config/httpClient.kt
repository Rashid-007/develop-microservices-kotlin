package com.shop.userservice.config

import com.typesafe.config.ConfigFactory
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.config.*

fun createHttpClient() = HttpClient(Apache) {
    install(JsonFeature) {
        serializer = JacksonSerializer()
    }
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.HEADERS
    }
}

val config = HoconApplicationConfig(ConfigFactory.load())
val itemServiceBaseUrl: String = config.propertyOrNull("shop.shop-item-service.baseUrl")?.getString() ?: "http://unknown"