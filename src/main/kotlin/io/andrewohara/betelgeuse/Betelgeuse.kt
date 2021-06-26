package io.andrewohara.betelgeuse

import javafx.application.Application
import javafx.collections.ObservableList
import javafx.stage.Stage

import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import redis.clients.jedis.Jedis
import redis.clients.jedis.ScanParams
import java.util.*


object Betelgeuse {

    @JvmStatic
    fun main(args: Array<String>) {
        Application.launch(MainApp::class.java, *args)
    }
}

class MainApp: Application() {

    companion object {
        private const val appName = "Betelgeuse - Graphical Redis Client"
    }

    override fun start(stage: Stage) {
        val client = Jedis("localhost", 6379)

        val valueView = ValueView(client)
        val keysView = KeysView(client) { valueView.lookupKey(it) }

        val layout = BorderPane().apply {
            top = menuBar()
            left = keysView
            center = valueView
        }

        val scene = Scene(layout, 640.toDouble(), 480.toDouble())

        stage.scene = scene
        stage.title = appName
        stage.show()
    }
}

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

class KeysView(private val client: Jedis, onClick: (String) -> Unit): BorderPane() {

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
            val item = listView.selectionModel.selectedItem
            onClick(item)
        }
        items = listView.items

        top = HBox(refreshButton, addButton)
        center = listView

        refresh()
    }

    var counter = 0
    private fun add() {
        client.set("key${counter++}", UUID.randomUUID().toString())
    }


    private fun refresh() {
        val newKeys = client.keySequence()

        items.clear()
        items.addAll(newKeys)
    }
}

private fun Jedis.keySequence(): Sequence<String> {
    var cursor = ""

    return sequence {
        while(cursor != ScanParams.SCAN_POINTER_START) {
            val result = scan(cursor)
            cursor = result.cursor
            yieldAll(result.result)
        }
    }
}

fun menuBar(): MenuBar {
    val createConnection = MenuItem("New")
    createConnection.setOnAction { println("connect") }

    val connectMenu = Menu("Connections")
    connectMenu.items += createConnection


    val bar = MenuBar()
    bar.menus += connectMenu
    return bar
}