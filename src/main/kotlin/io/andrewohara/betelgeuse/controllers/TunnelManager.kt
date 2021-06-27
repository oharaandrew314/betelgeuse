package io.andrewohara.betelgeuse.controllers

import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import java.util.*

class TunnelManager {

    private val jsch = JSch()

    init {
        jsch.addIdentity("C:\\code\\camcloud\\keys\\camcloud-development-andrew.pem.rsa")
    }

    private val config = Properties().apply {
        setProperty("StrictHostKeyChecking", "no")
    }

    fun connect(host: String, username: String, port: Int = 22): Tunnel {
        val session = jsch.getSession(username, host, port)
        session.setConfig(config)
        session.connect()

        return Tunnel(session)
    }

    class Tunnel(private val session: Session) {

        fun forward(host: String, remotePort: Int): Int {
            return session.setPortForwardingL(0, host, remotePort)
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val localPort = TunnelManager()
                .connect("54.197.216.197", "ec2-user")
                .forward("dev-cache-2.oc4ntm.0001.use1.cache.amazonaws.com", 6379)
            println("Connected on port $localPort")
        }
    }
}