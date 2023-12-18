package com.unchil.spacex

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform