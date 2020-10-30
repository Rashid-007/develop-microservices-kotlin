package com.shop.userservice.service

import com.shop.userservice.config.createHttpClient
import com.shop.userservice.config.itemServiceBaseUrl
import com.shop.userservice.domain.Item
import com.shop.userservice.domain.Profile
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory

/*class  ListField<T: Any>(var list: List<T>){
    operator fun getValue(): List<T> {

    }
    operator fun setValue(): List<T> {

    }
}*/

    val logger = LoggerFactory.getLogger("HttpClient_logger")!!

    fun getPurchasedItems(userProfile: Profile) : List<Item>? {
        return runBlocking {
           logger.info("Call to item-service started")
            with(createHttpClient()) {
                val purchasedItems = userProfile.purchasedItemsId?.map {
                    get<Item>("${itemServiceBaseUrl}/api/v1/item/${it}")
                } ?: listOf()
                close()
                logger.info("Call to item-service ended")
                purchasedItems
            }

        }
    }
