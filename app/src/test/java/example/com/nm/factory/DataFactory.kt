package example.com.nm.factory

import java.util.*
import java.util.concurrent.ThreadLocalRandom

object DataFactory {

    const val EMAIL = "nm@nm.com"

    fun randomString(): String {
        return UUID.randomUUID().toString()
    }

    fun randomInt(): Int {
        return ThreadLocalRandom.current().nextInt(0, 1000 + 1)
    }

    fun randomLong(): Long {
        return randomInt().toLong()
    }

    fun randomBoolean(): Boolean {
        return Math.random() < 0.5
    }

}