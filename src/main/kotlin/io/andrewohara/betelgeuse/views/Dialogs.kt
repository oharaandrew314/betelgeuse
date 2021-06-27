package io.andrewohara.betelgeuse.views

import io.andrewohara.betelgeuse.models.ConnectionData
import io.andrewohara.betelgeuse.models.ServerData
import javafx.scene.control.*
import javafx.scene.layout.GridPane
import javafx.scene.control.Alert.AlertType

import javafx.scene.control.Alert

object Dialogs {

    fun createConnection(): Dialog<ConnectionData> {
        return Dialog<ConnectionData>().apply {
            title = "Connect to Database"

            val content = GridPane()

            content.add(Label("Host"), 0, 0)
            val hostField = TextField().also { content.add(it, 1, 0) }

            content.add(Label("Port"), 0, 1)
            val portField = TextField().also {
//                it.textFormatter = TextFormatter(NumberStringConverter())
                content.add(it, 1, 1)
            }

            content.add(Label("Database"), 0, 2)
            val databaseField = TextField().also {
//                it.textFormatter = TextFormatter(NumberStringConverter())
                content.add(it, 1, 2)
            }

            content.add(Label("Name"), 0, 3)
            val nameField = TextField().also { content.add(it, 1, 3) }

            dialogPane.content = content

            dialogPane.buttonTypes += ButtonType("Save", ButtonBar.ButtonData.APPLY)
            dialogPane.buttonTypes += ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE)

            setResultConverter { dialogButton ->
                if (dialogButton.buttonData == ButtonBar.ButtonData.APPLY) {
                    ConnectionData(
                        host = hostField.text,
                        port = portField.text.toInt(),
                        database = databaseField.text.toInt(),
                        name = nameField.text
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