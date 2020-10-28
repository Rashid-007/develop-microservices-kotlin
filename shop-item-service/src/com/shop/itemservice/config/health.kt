package com.shop.itemservice.config

data class Health(val health: HealthStatus)

enum class HealthStatus{
    HEALTHY,
    OVERLOADED,
}