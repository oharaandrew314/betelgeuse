package io.andrewohara.betelgeuse

data class Connection(
    val host: String = "localhost",
    val port: Int = 6379,
    val database: Int = 0
)