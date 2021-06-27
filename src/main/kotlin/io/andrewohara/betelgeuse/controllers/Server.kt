package io.andrewohara.betelgeuse.controllers

import com.github.fppt.jedismock.RedisServer
import io.andrewohara.betelgeuse.models.ServerData

interface Server {
    val name: String

    fun start()
    fun stop()

    fun isRunning(): Boolean
}

class RedisMockServer(data: ServerData): Server {

    override val name = data.name
    private var running = false
    private val server = RedisServer.newRedisServer(data.port)

    override fun start() {
        server.start()
        running = true
    }

    override fun stop() {
        server.stop()
        running = false
    }

    override fun isRunning() = running
}