package io.andrewohara.betelgeuse.views

import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem

object Menus {

    fun menuBar(): MenuBar {
        val createConnection = MenuItem("New")
        createConnection.setOnAction {
            Dialogs.connectionDialog().showAndWait().ifPresent { data ->
                println(data.buttonData)
            }
        }

        val connectMenu = Menu("Connections")
        connectMenu.items += createConnection


        val bar = MenuBar()
        bar.menus += connectMenu
        return bar
    }
}