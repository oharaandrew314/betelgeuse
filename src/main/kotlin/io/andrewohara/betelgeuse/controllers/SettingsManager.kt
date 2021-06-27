package io.andrewohara.betelgeuse.controllers

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.andrewohara.betelgeuse.Betelgeuse
import io.andrewohara.betelgeuse.models.ConnectionData
import io.andrewohara.betelgeuse.models.ServerData
import java.util.prefs.Preferences

interface SettingsManager {
    fun updateConnections(connections: Collection<ConnectionData>)
    fun getConnections(): List<ConnectionData>

    fun updateSelectedConnection(name: String?)
    fun getSelectedConnection(): String?

    fun getServers(): List<ServerData>
    fun updateServers(data: List<ServerData>)
}

class PreferencesSettingsManager: SettingsManager {

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val connectionsAdapter = moshi.adapter(Array<ConnectionData>::class.java)
    private val serverAdapter = moshi.adapter(Array<ServerData>::class.java)

    private val preferences = Preferences.userNodeForPackage(Betelgeuse::class.java)

    override fun updateConnections(connections: Collection<ConnectionData>) {
        val json = connectionsAdapter.toJson(connections.toTypedArray())
        preferences.put("connections", json)
    }

    override fun getConnections(): List<ConnectionData> {
        val json = preferences.get("connections", "[]")
        return connectionsAdapter.fromJson(json)?.toList() ?: emptyList()
    }

    override fun updateSelectedConnection(name: String?) {
        preferences.put("selected", name ?: "")
    }

    override fun getSelectedConnection(): String? {
        return preferences.get("selected", null)
    }

    override fun getServers(): List<ServerData> {
        val json = preferences.get("servers", "[]")
        return serverAdapter.fromJson(json)?.toList() ?: emptyList()
    }

    override fun updateServers(data: List<ServerData>) {
        val json = serverAdapter.toJson(data.toTypedArray())
        preferences.put("servers", json)
    }
}