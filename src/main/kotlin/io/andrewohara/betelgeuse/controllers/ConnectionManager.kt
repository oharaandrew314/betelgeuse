package io.andrewohara.betelgeuse.controllers

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.andrewohara.betelgeuse.Betelgeuse
import io.andrewohara.betelgeuse.ConnectionData
import redis.clients.jedis.Jedis
import java.util.prefs.Preferences

class ConnectionManager {

    private val connections = mutableListOf<ConnectionData>()
    private var selected: ConnectionData? = null

    private val prefs = Preferences.userNodeForPackage(Betelgeuse::class.java)

    private val jsonAdapter = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
        .adapter(Array<ConnectionData>::class.java)

    init {
        load()
    }

    fun createConnection(data: ConnectionData) {
        connections += data
        selected = data
        save()
    }

    fun selectConnection(connectionData: ConnectionData) {
        selected = connectionData
        save()
    }

    fun connections() = connections.toList()
    fun selected() = selected

    fun getConnection(): RedisConnection? = selected?.let {
        val jedis = Jedis(it.host, it.port)
        jedis.select(it.database)
        JedisConnection(jedis)
    }

    private fun save() {
        val json = jsonAdapter.toJson(connections.toTypedArray())
        prefs.put("connections", json)
        prefs.put("selected", selected?.name ?: "")
    }

    private fun load() {
        connections.clear()

        val json = prefs.get("connections", "[]")
        val loadedConnections = jsonAdapter.fromJson(json) ?: emptyArray()
        connections += loadedConnections

        val selectedName = prefs.get("selected", null)
        selected = connections
            .firstOrNull { it.name == selectedName }
            ?: connections.firstOrNull()
    }
}