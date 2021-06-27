package io.andrewohara.betelgeuse.controllers

import redis.clients.jedis.Jedis
import redis.clients.jedis.ScanParams

interface RedisConnection {
    fun keys(): Sequence<String>
    operator fun get(key: String): String?
    operator fun set(key: String, value: String)
}

class JedisConnection(private val jedis: Jedis): RedisConnection {

    override fun keys(): Sequence<String> {
        var cursor = ""

        return sequence {
            while(cursor != ScanParams.SCAN_POINTER_START) {
                val result = jedis.scan(cursor)
                cursor = result.cursor
                yieldAll(result.result)
            }
        }
    }

    override fun get(key: String): String? = jedis.get(key)

    override fun set(key: String, value: String) {
        jedis.set(key, value)
    }
}