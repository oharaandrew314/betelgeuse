package io.andrewohara.betelgeuse.views

import io.andrewohara.betelgeuse.ConnectionData
import javafx.scene.control.*
import javafx.scene.layout.GridPane

object Dialogs {

    fun connectionDialog(): Dialog<ConnectionData> {
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
}