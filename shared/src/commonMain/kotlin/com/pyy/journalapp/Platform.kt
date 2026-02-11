package com.pyy.journalapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform