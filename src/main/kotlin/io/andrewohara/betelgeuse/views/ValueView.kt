package io.andrewohara.betelgeuse.views

import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.layout.BorderPane
import redis.clients.jedis.Jedis

class ValueView(private val client: Jedis): BorderPane() {

    private val field = TextArea()
    private var currentKey: String? = null

    init {
        val saveButton = Button("Save")
        saveButton.setOnAction { save() }


        center = field
        bottom = saveButton
    }

    fun lookupKey(key: String) {
        currentKey = key
        field.text = client.get(key)
    }

    private fun save() {
        if (currentKey == null) return

        client.set(currentKey, field.text)
    }
}