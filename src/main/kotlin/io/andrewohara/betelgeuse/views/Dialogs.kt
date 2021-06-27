package io.andrewohara.betelgeuse.views

import io.andrewohara.betelgeuse.models.ConnectionData
import io.andrewohara.betelgeuse.models.ServerData
import io.andrewohara.betelgeuse.models.TunnelData
import javafx.collections.FXCollections
import javafx.scene.control.*
import javafx.scene.layout.GridPane
import javafx.scene.control.Alert.AlertType

import javafx.scene.control.Alert
import javafx.scene.layout.VBox

object Dialogs {

    private enum class TunnelOptions {
        None, SshTunnel
    }

    fun createConnection(): Dialog<ConnectionData> {
        return Dialog<ConnectionData>().apply {
            title = "Connect to Database"

            val hostContent = GridPane()

            hostContent.add(Label("Host"), 0, 0)
            val hostField = TextField().also {
                it.promptText = "localhost"
                hostContent.add(it, 1, 0)
            }

            hostContent.add(Label("Port"), 0, 1)
            val portField = TextField().also {
                it.promptText = "6379"
//                it.textFormatter = TextFormatter(NumberStringConverter())
                hostContent.add(it, 1, 1)
            }

            hostContent.add(Label("Database"), 0, 2)
            val databaseField = TextField().also {
                it.promptText = "0"
//                it.textFormatter = TextFormatter(NumberStringConverter())
                hostContent.add(it, 1, 2)
            }

            hostContent.add(Label("Name"), 0, 3)
            val nameField = TextField().also { hostContent.add(it, 1, 3) }

            val options = FXCollections.observableList(TunnelOptions.values().toList())
            hostContent.add(Label("Proxy"), 0, 4)
            val proxyField = ComboBox(options).also {
                hostContent.add(it,1, 4)
            }

            val tunnelContent = GridPane()

            tunnelContent.add(Label("Tunnel Host"), 0, 0)
            val tunnelHostField = TextField().also { tunnelContent.add(it, 1, 0) }

            tunnelContent.add(Label("Tunnel Port"), 0, 1)
            val tunnelPortField = TextField().also {
                it.promptText = "22"
                tunnelContent.add(it, 1, 1)
            }

            tunnelContent.add(Label("Tunnel Username"), 0, 2)
            val tunnelUsernameField = TextField().also { tunnelContent.add(it, 1, 2) }

            tunnelContent.add(Label("Tunnel Keyfile"), 0, 3)
            val tunnelKeyfileField = TextField().also { tunnelContent.add(it, 1, 3) }


            proxyField.selectionModel.selectedItemProperty().addListener { _, _, selected ->
                when(selected) {
                    TunnelOptions.None, null -> tunnelContent.isVisible = false
                    TunnelOptions.SshTunnel -> tunnelContent.isVisible = true
                }
            }
            proxyField.selectionModel.selectFirst()


            dialogPane.content = VBox(hostContent, tunnelContent)

            dialogPane.buttonTypes += ButtonType("Save", ButtonBar.ButtonData.APPLY)
            dialogPane.buttonTypes += ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE)

            setResultConverter { dialogButton ->
                if (dialogButton.buttonData == ButtonBar.ButtonData.APPLY) {
                    ConnectionData(
                        host = hostField.text,
                        port = portField.text.toInt(),
                        database = databaseField.text.toInt(),
                        name = nameField.text,
                        proxy = if (proxyField.selectionModel.selectedItem == TunnelOptions.SshTunnel) {
                            TunnelData(
                                host = tunnelHostField.text,
                                port = tunnelPortField.text.toInt(),
                                username = tunnelUsernameField.text,
                                keyFilePath = tunnelKeyfileField.text
                            )
                        } else null
                    )
                } else null
            }
        }
    }

    fun createKey(): Dialog<String> {
        val dialog = TextInputDialog("")
        dialog.headerText = "Create a new Entry"
        dialog.contentText = "key"
        return dialog
    }

    fun confirmDelete(key: String): Alert {
        val alert = Alert(AlertType.CONFIRMATION)
        alert.title = "Delete"
        alert.headerText = "Delete $key"
        alert.contentText = "Are you want to delete this?"
        return alert
    }

    fun createServer(): Dialog<ServerData> {
        return Dialog<ServerData>().apply {
            title = "Create Server"

            val content = GridPane()

            content.add(Label("Name"), 0, 0)
            val nameField = TextField().also {
                content.add(it, 1, 0)
            }

            content.add(Label("Port"), 0, 1)
            val portField = TextField().also {
                content.add(it, 1, 1)
            }

            dialogPane.content = content

            dialogPane.buttonTypes += ButtonType("Save", ButtonBar.ButtonData.APPLY)
            dialogPane.buttonTypes += ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE)

            setResultConverter { dialogButton ->
                if (dialogButton.buttonData == ButtonBar.ButtonData.APPLY) {
                    ServerData(name = nameField.text, port = portField.text.toInt())
                } else null
            }
        }
    }
}