package io.andrewohara.betelgeuse.views

import io.andrewohara.betelgeuse.controllers.ServerStatus
import io.andrewohara.betelgeuse.models.ConnectionData
import io.andrewohara.betelgeuse.models.ServerData
import javafx.scene.control.*

object Menus {

    fun menuBar(
        createConnection: (ConnectionData) -> Unit,
        getConnections: () -> List<ConnectionData>,
        currentConnection: () -> ConnectionData?,
        selectConnection: (ConnectionData) -> Unit,
        createServer: (ServerData) -> Unit,
        getServers: () -> List<ServerStatus>,
        toggleServer: (ServerStatus) -> Unit
    ): MenuBar {
        val newConnection = MenuItem("Create")
        newConnection.setOnAction {
            Dialogs.createConnection().showAndWait().ifPresent(createConnection)
        }

        val connectMenu = Menu("Connections")
        connectMenu.items += newConnection
        connectMenu.items += SeparatorMenuItem()
        connectMenu.setOnShowing {
            connectMenu.items.removeIf { it is RadioMenuItem }
            for (connection in getConnections()) {
                connectMenu.items += RadioMenuItem(connection.name).apply {
                    isSelected = connection == currentConnection()
                    setOnAction { selectConnection(connection) }
                }
            }
        }

        val createServerItem = MenuItem("Create")
        createServerItem.setOnAction {
            Dialogs.createServer().showAndWait().ifPresent { data ->
                createServer(data)
            }
        }

        val serversMenu = Menu("Servers")
        serversMenu.items += createServerItem
        serversMenu.items += SeparatorMenuItem()
        serversMenu.setOnShowing {
            serversMenu.items.removeIf { it is CheckMenuItem }
            for (server in getServers()) {
                serversMenu.items += CheckMenuItem("${server.name}: ${server.data.port}").apply {
                    isSelected = server.running
                    setOnAction { toggleServer(server) }
                }
            }
        }

        val bar = MenuBar()
        bar.menus += connectMenu
        bar.menus += serversMenu
        return bar
    }
}