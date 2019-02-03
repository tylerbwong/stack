package me.tylerbwong.stack.data.utils

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.channels.Channel

suspend fun <T> concat(vararg sources: Deferred<T>): Channel<T> {
    val channel = Channel<T>(sources.size)

    sources.forEach {
        channel.offer(it.await())
    }
    channel.close()

    return channel
}
