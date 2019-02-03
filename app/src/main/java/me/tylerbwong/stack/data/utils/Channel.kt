package me.tylerbwong.stack.data.utils

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel

suspend fun <T> concat(vararg sources: Deferred<T>): ReceiveChannel<T> {
    val channel = Channel<T>(sources.size)

    sources.forEach {
        try {
            channel.offer(it.await())
        } catch (ex: Exception) {
            channel.close(ex)
        }
    }

    channel.close()

    return channel
}
