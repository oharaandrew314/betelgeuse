package io.andrewohara.betelgeuse.controllers

import com.jcraft.jsch.JSch
import io.andrewohara.betelgeuse.models.ConnectionData
import redis.clients.jedis.Jedis
import java.nio.file.Files
import java.util.*

class ConnectionManager(private val settings: SettingsManager) {

    companion object {
        private val jschConfig = Properties().apply {
            setProperty("StrictHostKeyChecking", "no")
        }
    }

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
        val jedis = if (proxy == null) {
            Jedis(host, port)
        } else {
            val jsch = JSch()
            jsch.addIdentity(proxy.keyFilePath)

            val session = jsch.getSession(proxy.username, proxy.host, proxy.port)
            session.setConfig(jschConfig)
            session.connect()

            val localPort = session.setPortForwardingL(0, host, port)
            Jedis("localhost", localPort)
        }

        jedis.select(database)
        return JedisConnection(jedis)
    }
}