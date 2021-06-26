package io.andrewohara.betelgeuse

import io.andrewohara.betelgeuse.views.KeysView
import io.andrewohara.betelgeuse.views.Menus
import io.andrewohara.betelgeuse.views.ValueView
import javafx.application.Application
import javafx.stage.Stage

import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import redis.clients.jedis.Jedis


class Betelgeuse: Application() {

    companion object {
        private const val appName = "Betelgeuse - Redis Client"

        @JvmStatic
        fun main(args: Array<String>) {
            launch(Betelgeuse::class.java, *args)
        }
    }

    override fun start(stage: Stage) {
        val client = Jedis("localhost", 6379)

        val valueView = ValueView(client)
        val keysView = KeysView(client) { valueView.lookupKey(it) }

        val layout = BorderPane().apply {
            top = Menus.menuBar()
            left = keysView
            center = valueView
        }

        val scene = Scene(layout, 640.toDouble(), 480.toDouble())

        stage.scene = scene
        stage.title = appName
        stage.show()
    }
}