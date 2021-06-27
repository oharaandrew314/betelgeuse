package io.andrewohara.betelgeuse.models

data class ConnectionData(
    val host: String = "localhost",
    val port: Int = 6379,
    val database: Int = 0,
    val name: String
)