package com.shop.itemservice.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm

open class SimpleJWT(secret: String){
    val algorithm = Algorithm.HMAC256(secret)
    val verifier = JWT.require(algorithm).build()

    fun sign(name: String): String = JWT.create().withClaim("name", name).sign(algorithm)
}

val simpleJwt =
    SimpleJWT("my-super-secret-for-jwt")