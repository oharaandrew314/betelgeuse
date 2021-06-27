package io.andrewohara.betelgeuse.views

import io.andrewohara.betelgeuse.controllers.RedisConnection
import javafx.collections.ObservableList
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox

class KeysView(
    private val getClient: () -> RedisConnection?,
    selectKey: (String) -> Unit
): BorderPane() {

    companion object {
        private const val limit = 1000
    }

    private var keys = listOf<String>()
    private val displayedKeys: ObservableList<String>

    init {
        val refreshButton = Button("Refresh")
        refreshButton.setOnAction { refresh() }

        val listView = ListView<String>()
        listView.setOnMouseClicked {
            val item = listView.selectionModel.selectedItem ?: return@setOnMouseClicked
            selectKey(item)
        }
        displayedKeys = listView.items

        val searchField = TextField().apply {
            promptText = "Search"
            setOnKeyTyped { _ ->
                val filtered = keys.filter { key -> text in key }
                displayedKeys.setAll(filtered)
            }
        }

        val addButton = Button("Create")
        addButton.setOnAction {
            Dialogs.createKey().showAndWait().ifPresent { key ->
                displayedKeys += key
                listView.selectionModel.select(key)
                selectKey(key)
            }
        }

        top = HBox(refreshButton, searchField)
        center = listView
        bottom = addButton

        refresh()
    }


    fun refresh() {
        val client = getClient() ?: return
        keys = client.keys().take(limit).toList()

        displayedKeys.setAll(keys)
    }
}