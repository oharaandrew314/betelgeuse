package io.andrewohara.betelgeuse.views

import io.andrewohara.betelgeuse.jedis.keySequence
import javafx.collections.ObservableList
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import redis.clients.jedis.Jedis
import java.util.*

class KeysView(private val client: () -> Jedis?, onClick: (String) -> Unit): BorderPane() {

    private val items: ObservableList<String>

    init {
        val refreshButton = Button("Refresh")
        refreshButton.setOnAction { refresh() }

        val addButton = Button("+")
        addButton.setOnAction {
            add()
            refresh()
        }

        val listView = ListView<String>()
        listView.setOnMouseClicked {
            val item = listView.selectionModel.selectedItem ?: return@setOnMouseClicked
            onClick(item)
        }
        items = listView.items

        top = HBox(refreshButton, addButton)
        center = listView

        refresh()
    }

    var counter = 0
    private fun add() {
        client()?.set("key${counter++}", UUID.randomUUID().toString())
    }


    fun refresh() {
        val newKeys = client()?.keySequence() ?: emptySequence()

        items.clear()
        items.addAll(newKeys)
    }
}