package io.andrewohara.betelgeuse.models

data class ConnectionData(
    val host: String,
    val port: Int,
    val database: Int,
    val name: String,
    val proxy: TunnelData?
)