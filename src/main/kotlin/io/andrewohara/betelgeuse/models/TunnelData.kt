package io.andrewohara.betelgeuse.models

data class TunnelData(
    val host: String,
    val username: String,
    val port: Int = 22,
    val keyFilePath: String
)