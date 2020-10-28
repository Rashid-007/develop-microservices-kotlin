package com.shop.userservice.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.application.ApplicationCall
import io.ktor.auth.OAuthServerSettings
import io.ktor.features.origin
import io.ktor.http.HttpMethod
import io.ktor.request.host
import io.ktor.request.port

open class SimpleJWT(secret: String){
    val algorithm = Algorithm.HMAC256(secret)
    val verifier = JWT.require(algorithm).build()

    fun sign(name: String): String = JWT.create().withClaim("name", name).sign(algorithm)
}

val simpleJwt =
    SimpleJWT("my-super-secret-for-jwt")

var googleOauthProvider = OAuthServerSettings.OAuth2ServerSettings(
    name = "google",
    authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
    accessTokenUrl = "https://www.googleapis.com/oauth2/v3/token",
    requestMethod = HttpMethod.Post,
    clientId = "833250522031-j9h6qrss0n0h6k0e6skbmbhdnasrnljr.apps.googleusercontent.com",
    clientSecret = "LFFk9baRLikMzCBnFo4umB-v",
    defaultScopes = listOf("profile")
)

fun ApplicationCall.redirectUrl(path: String): String {
    val defaultPort = if (request.origin.scheme == "http") 80 else 443
    val hostPort = request.host()!! + request.port().let { port -> if (port == defaultPort) "" else ":$port" }
    val protocol = request.origin.scheme
    return "$protocol://$hostPort$path"
}

class JustSellSession(val notUserId: String)