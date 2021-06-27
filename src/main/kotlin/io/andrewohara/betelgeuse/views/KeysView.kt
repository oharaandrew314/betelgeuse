package io.andrewohara.betelgeuse.views

import javafx.beans.Observable
import javafx.collections.ObservableList
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox

class KeysView(
    private val selectKey: (String?) -> Unit,
    handleDelete: (String) -> Boolean,
    private val handleRefresh: (KeysView) -> Unit
): BorderPane() {


    private var keys = mutableListOf<String>()
    private val displayedKeys: ObservableList<String>
    private val searchField: TextField

    init {

        val listView = ListView<String>()
        listView.selectionModel.selectedItemProperty().addListener { _ :Observable ->
            val selected = listView.selectionModel.selectedItem ?: return@addListener
            selectKey(selected)
        }
        displayedKeys = listView.items

        searchField = TextField().apply {
            promptText = "Search"
            setOnKeyTyped { _ ->
                val filtered = keys.filter { key -> text in key }
                displayedKeys.setAll(filtered)
            }
        }

        val refreshButton = Button("Refresh")
        refreshButton.setOnAction { handleRefresh(this) }

        val addButton = Button("Create")
        addButton.setOnAction {
            Dialogs.createKey().showAndWait().ifPresent { key ->
                displayedKeys += key
                listView.selectionModel.select(key)
                selectKey(key)
            }
        }

        listView.setOnKeyPressed { event ->
            val selected = listView.selectionModel.selectedItem ?: return@setOnKeyPressed
            if (event.code == KeyCode.DELETE) {
                if (handleDelete(selected)) {
                    keys.remove(selected)
                    displayedKeys.remove(selected)
                }
            }
        }

        top = HBox(refreshButton, searchField)
        center = listView
        bottom = addButton
    }

    fun update(newKeys: List<String>) {
        keys = newKeys.toMutableList()
        displayedKeys.setAll(keys)

        searchField.text = ""
        selectKey(null)
    }
}