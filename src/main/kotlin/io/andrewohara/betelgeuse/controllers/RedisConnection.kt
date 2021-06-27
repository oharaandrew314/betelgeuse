package io.andrewohara.betelgeuse.controllers

import redis.clients.jedis.Jedis
import redis.clients.jedis.ScanParams
import java.net.SocketTimeoutException

interface RedisConnection {
    fun keys(): Sequence<String>
    operator fun get(key: String): String?
    operator fun set(key: String, value: String)
    fun delete(key: String)
}

class JedisConnection(private val jedis: Jedis): RedisConnection {

    override fun keys(): Sequence<String> {
        var cursor = ScanParams.SCAN_POINTER_START

        return try {
            sequence {
                do {
                    val result = jedis.scan(cursor)
                    cursor = result.cursor
                    yieldAll(result.result)
                } while (cursor != ScanParams.SCAN_POINTER_START)
            }
        } catch (e: SocketTimeoutException) {
            emptySequence()
        }
    }

    override fun get(key: String): String? = try {
        jedis.get(key)
    } catch (e: SocketTimeoutException) {
        null
    }

    override fun set(key: String, value: String) {
        try {
            jedis.set(key, value)
        } catch (e: SocketTimeoutException) {
            // TODO return error
        }
    }

    override fun delete(key: String) {
        try {
            jedis.del(key)
        } catch (e: SocketTimeoutException) {
            // TODO return error
        }
    }
}