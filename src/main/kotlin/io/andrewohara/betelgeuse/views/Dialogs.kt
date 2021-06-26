package io.andrewohara.betelgeuse.views

import javafx.scene.control.ButtonBar
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog

object Dialogs {

    fun connectionDialog(): Dialog<ButtonType> {
        return Dialog<ButtonType>().apply {
            title = "Create Connection"
            contentText = "Create a new Connection"

            dialogPane.buttonTypes += ButtonType("Save", ButtonBar.ButtonData.APPLY)
            dialogPane.buttonTypes += ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE)
        }
    }
}