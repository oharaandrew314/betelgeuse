package io.andrewohara.betelgeuse.views

import io.andrewohara.betelgeuse.ConnectionData
import javafx.scene.control.*

object Menus {

    fun menuBar(
        createConnection: (ConnectionData) -> Unit,
        getConnections: () -> List<ConnectionData>,
        currentConnection: () -> ConnectionData?,
        selectConnection: (ConnectionData) -> Unit
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

        val bar = MenuBar()
        bar.menus += connectMenu
        return bar
    }
}