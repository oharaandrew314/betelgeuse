package io.andrewohara.betelgeuse.views

import io.andrewohara.betelgeuse.controllers.RedisConnection
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.layout.BorderPane

class ValueView(private val client: () -> RedisConnection?): BorderPane() {

    private val field = TextArea()
    private var currentKey: String? = null

    init {
        val saveButton = Button("Save")
        saveButton.setOnAction { save() }


        center = field
        bottom = saveButton
    }

    fun lookupKey(key: String?) {
        currentKey = key
        field.text = if (key == null) "" else client()?.get(key)
    }

    private fun save() {
        val client = client() ?: return
        val key = currentKey ?: return

        client[key] = field.text
    }
}