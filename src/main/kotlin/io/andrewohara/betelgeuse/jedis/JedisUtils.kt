package io.andrewohara.betelgeuse.jedis

import redis.clients.jedis.Jedis
import redis.clients.jedis.ScanParams

fun Jedis.keySequence(): Sequence<String> {
    var cursor = ""

    return sequence {
        while(cursor != ScanParams.SCAN_POINTER_START) {
            val result = scan(cursor)
            cursor = result.cursor
            yieldAll(result.result)
        }
    }
}