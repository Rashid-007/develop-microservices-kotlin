package com.shop.userservice.config

data class Health(val health: HealthStatus)

enum class HealthStatus{
    HEALTHY,
    OVERLOADED,
}