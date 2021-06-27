package io.andrewohara.betelgeuse.controllers

import io.andrewohara.betelgeuse.models.ServerData
import org.slf4j.LoggerFactory

class ServerManager(private val settings: SettingsManager) {

    private val log = LoggerFactory.getLogger(javaClass)

    private val models = settings.getServers().toMutableList()
    private val servers = models.map { RedisMockServer(it) }.toMutableList()

    fun list() = models.mapNotNull {
        val server = servers.firstOrNull { server -> server.name == it.name } ?: return@mapNotNull null
        ServerStatus(data = it, running = server.isRunning() )
    }

    fun createServer(model: ServerData) {
        models += model
        servers += RedisMockServer(model)

        settings.updateServers(models)
        log.info("Created $model")
    }

    fun deleteServer(model: ServerData) {
        models -= model

        val server = servers.firstOrNull { it.name == model.name }
        if (server != null) {
            server.stop()
            servers.remove(server)
        }

        settings.updateServers(models)
    }

    fun startServer(model: ServerData) {
        val server = servers.firstOrNull { it.name == model.name } ?: return
        server.start()
        log.info("Started $model")
    }

    fun stopServer(model: ServerData) {
        val server = servers.firstOrNull { it.name == model.name } ?: return
        server.stop()
        log.info("Stopped $model")
    }

    fun stopAll() {
        servers
            .filter { it.isRunning() }
            .forEach { it.stop() }
    }
}