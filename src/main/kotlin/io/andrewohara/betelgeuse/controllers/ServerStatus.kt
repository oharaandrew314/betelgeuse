package io.andrewohara.betelgeuse.controllers

import io.andrewohara.betelgeuse.models.ServerData

data class ServerStatus(
    val data: ServerData,
    val running: Boolean
) {
    val name = data.name
}