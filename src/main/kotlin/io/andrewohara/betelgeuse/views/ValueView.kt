package io.andrewohara.betelgeuse.views

import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.layout.BorderPane

class ValueView(handleSave: (String, String) -> Unit): BorderPane() {

    private val field = TextArea()
    private var currentKey: String? = null

    init {
        val saveButton = Button("Save")
        saveButton.setOnAction {
            currentKey?.let { key ->
                handleSave(key, field.text)
            }
        }


        center = field
        bottom = saveButton
    }

    fun set(key: String?, value: String?) {
        currentKey = key
        field.text = value ?: ""
    }
}