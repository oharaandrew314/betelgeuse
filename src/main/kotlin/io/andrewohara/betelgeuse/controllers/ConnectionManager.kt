package io.andrewohara.betelgeuse.controllers

import io.andrewohara.betelgeuse.models.ConnectionData
import redis.clients.jedis.Jedis

class ConnectionManager(private val settings: SettingsManager) {

    private val connections = settings.getConnections().toMutableList()
    private var selected = settings.getSelectedConnection()?.let { name ->
        connections.firstOrNull { it.name == name }
    }

    fun createConnection(data: ConnectionData): RedisConnection {
        connections += data
        selected = data

        settings.updateConnections(connections)

        return data.connect()
    }

    fun selectConnection(connectionData: ConnectionData): RedisConnection {
        selected = connectionData
        settings.updateSelectedConnection(selected?.name)
        return connectionData.connect()
    }

    fun connections() = connections.toList()
    fun selected() = selected

    fun getConnection(): RedisConnection? = selected?.connect()

    private fun ConnectionData.connect(): RedisConnection {
        val jedis = Jedis(host, port)
        jedis.select(database)
        return JedisConnection(jedis)
    }
}