package io.andrewohara.betelgeuse.views

import io.andrewohara.betelgeuse.jedis.keySequence
import javafx.collections.ObservableList
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.layout.BorderPane
import redis.clients.jedis.Jedis

class KeysView(
    private val client: () -> Jedis?,
    selectKey: (String) -> Unit
): BorderPane() {

    private val items: ObservableList<String>

    init {
        val refreshButton = Button("Refresh")
        refreshButton.setOnAction { refresh() }

        val listView = ListView<String>()
        listView.setOnMouseClicked {
            val item = listView.selectionModel.selectedItem ?: return@setOnMouseClicked
            selectKey(item)
        }
        items = listView.items

        val addButton = Button("Create")
        addButton.setOnAction {
            Dialogs.createKey().showAndWait().ifPresent { key ->
                items += key
                listView.selectionModel.select(key)
                selectKey(key)
            }
        }

        top = refreshButton
        center = listView
        bottom = addButton

        refresh()
    }


    fun refresh() {
        val newKeys = client()?.keySequence() ?: emptySequence()

        items.clear()
        items.addAll(newKeys)
    }
}