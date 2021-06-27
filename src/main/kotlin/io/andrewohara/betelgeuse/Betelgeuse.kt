package io.andrewohara.betelgeuse

import io.andrewohara.betelgeuse.controllers.ConnectionManager
import io.andrewohara.betelgeuse.controllers.PreferencesSettingsManager
import io.andrewohara.betelgeuse.controllers.ServerManager
import io.andrewohara.betelgeuse.models.ConnectionData
import io.andrewohara.betelgeuse.views.Dialogs
import io.andrewohara.betelgeuse.views.KeysView
import io.andrewohara.betelgeuse.views.Menus
import io.andrewohara.betelgeuse.views.ValueView
import javafx.application.Application
import javafx.application.Platform
import javafx.stage.Stage

import javafx.scene.Scene
import javafx.scene.control.ButtonType
import javafx.scene.layout.BorderPane
import org.slf4j.LoggerFactory
import kotlin.system.exitProcess

class Betelgeuse: Application() {

    companion object {
        private const val appName = "Betelgeuse - Redis Manager"
        private const val keyPageSize = 1000
        private val defaultWindowSize = 640.toDouble() to 480.toDouble()

        @JvmStatic
        fun main(args: Array<String>) {
            launch(Betelgeuse::class.java, *args)
        }
    }

    private val log = LoggerFactory.getLogger(javaClass)

    private val settings = PreferencesSettingsManager()
    private val connectionManager = ConnectionManager(settings)
    private val serverManager = ServerManager(settings)

    private fun updateKeys(view: KeysView) {
        Thread {
            val connection = connectionManager.getConnection() ?: return@Thread
            val keys = connection.keys().take(keyPageSize).toList()
            Platform.runLater {
                view.update(keys)
            }
        }.start()
    }

    override fun start(stage: Stage) {
        val valueView = ValueView(
            handleSave = { key, value ->
                val connection = connectionManager.getConnection() ?: return@ValueView
                Thread {
                    connection[key] = value
                }.start()
            }
        )

        val keysView = KeysView(
            selectKey = { key ->
                log.info("Selecting key: $key")
                val connection = connectionManager.getConnection() ?: return@KeysView
                Thread {
                    val value = key?.let { connection[it] }
                    Platform.runLater { valueView.set(key, value) }
                }.start()
            },
            handleDelete = { key ->
                val result = Dialogs.confirmDelete(key).showAndWait()
                val confirm = result.orElseGet { ButtonType.CANCEL } == ButtonType.OK
                if (confirm) {
                    connectionManager.getConnection()?.delete(key)
                }

                confirm
            },
            handleRefresh = { updateKeys(it) }
        )

        fun selectConnection(data: ConnectionData) {
            connectionManager.selectConnection(data)
            updateKeys(keysView)
        }

        val menuBar = Menus.menuBar(
            createConnection = { data ->
                connectionManager.createConnection(data)
                selectConnection(data)
           },
            getConnections = { connectionManager.connections() },
            currentConnection = { connectionManager.selected() },
            selectConnection = { selectConnection(it) },
            createServer = { data ->
                serverManager.createServer(data)
            },
            getServers = { serverManager.list() },
            toggleServer = { status ->
                if (status.running) {
                    serverManager.stopServer(status.data)
                } else {
                    serverManager.startServer(status.data)
                }
            }
        )

        val layout = BorderPane().apply {
            top = menuBar
            left = keysView
            center = valueView
        }

        stage.scene = Scene(layout, defaultWindowSize.first, defaultWindowSize.second)
        stage.title = appName
        stage.setOnCloseRequest {
            serverManager.stopAll()
            exitProcess(0)
        }
        stage.show()

        updateKeys(keysView)
    }
}