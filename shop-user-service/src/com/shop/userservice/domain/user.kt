package com.shop.userservice.domain

import java.util.*

class User(val username: String, val password: String)

val users = Collections.synchronizedMap(
    listOf(User("test", "test"))
        .associateBy { it.username }
        .toMutableMap()
)

class UserRequestDto(val username: String, val password: String)