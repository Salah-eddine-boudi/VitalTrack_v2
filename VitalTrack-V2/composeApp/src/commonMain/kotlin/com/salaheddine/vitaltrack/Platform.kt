package com.salaheddine.vitaltrack

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform