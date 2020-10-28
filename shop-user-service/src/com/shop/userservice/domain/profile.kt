package com.shop.userservice.domain

import com.shop.userservice.service.getPurchasedItems
import java.util.*

data class Profile(
    val id: String = UUID.randomUUID().toString(),
    val firstname: String,
    val lastname: String,
    var purchasedItemsId: List<Int>? = null
) {
    val purchasedItems: List<Item>? by lazy { getPurchasedItems(this) }
}

val profiles = Collections.synchronizedMap(
    listOf(Profile(firstname = "test", lastname = "test"))
        .associateBy { it.id }
        .toMutableMap()
)