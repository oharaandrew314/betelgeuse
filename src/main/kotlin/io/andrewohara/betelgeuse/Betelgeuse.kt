package io.andrewohara.betelgeuse

import io.andrewohara.betelgeuse.controllers.ConnectionManager
import io.andrewohara.betelgeuse.views.KeysView
import io.andrewohara.betelgeuse.views.Menus
import io.andrewohara.betelgeuse.views.ValueView
import javafx.application.Application
import javafx.stage.Stage

import javafx.scene.Scene
import javafx.scene.layout.BorderPane

class Betelgeuse: Application() {

    companion object {
        private const val appName = "Betelgeuse - Redis Client"
        private val defaultWindowSize = 640.toDouble() to 480.toDouble()

        @JvmStatic
        fun main(args: Array<String>) {
            launch(Betelgeuse::class.java, *args)
        }
    }

    override fun start(stage: Stage) {
        try {
            val connectionManager = ConnectionManager()

            val valueView = ValueView { connectionManager.getConnection() }
            val keysView = KeysView(
                getClient = { connectionManager.getConnection() },
                selectKey = { valueView.lookupKey(it) }
            )
            val menuBar = Menus.menuBar(
                createConnection = { connectionManager.createConnection(it) },
                getConnections = { connectionManager.connections() },
                currentConnection = { connectionManager.selected() },
                selectConnection = {
                    connectionManager.selectConnection(it)
                    keysView.refresh() // FIXME these components should all listen for a connection-change event
                    valueView.lookupKey(null)
                }
            )

            val layout = BorderPane().apply {
                top = menuBar
                left = keysView
                center = valueView
            }

            val scene = Scene(layout, defaultWindowSize.first, defaultWindowSize.second)

            stage.scene = scene
            stage.title = appName
            stage.show()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}