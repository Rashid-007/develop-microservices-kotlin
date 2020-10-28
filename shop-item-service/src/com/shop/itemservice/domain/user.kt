package com.shop.itemservice.domain

import java.util.*

class User(val username: String, val password: String)

val users = Collections.synchronizedMap(
    listOf(User("test", "test"))
        .associateBy { it.username }
        .toMutableMap()
)

class LoginRegister(val username: String, val password: String)