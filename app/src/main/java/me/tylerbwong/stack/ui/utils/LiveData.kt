package me.tylerbwong.stack.ui.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <T, Y, Z> LiveData<T>.zipWith(
    source: LiveData<Y>,
    initialValue: Z? = null,
    zipFunction: (T, Y) -> Z
): LiveData<Z> {
    return MediatorLiveData<Z>().apply {
        initialValue?.let {
            value = it
        }

        var hasReceiverEmitted = false
        var receiverValue: T? = null

        var hasSourceEmitted = false
        var sourceValue: Y? = null

        addSource(this@zipWith) {
            hasReceiverEmitted = true
            receiverValue = it
            if (hasReceiverEmitted && hasSourceEmitted) {
                value = zipFunction(receiverValue!!, sourceValue!!)
                hasReceiverEmitted = false
                hasSourceEmitted = false
            }
        }

        addSource(source) {
            hasSourceEmitted = true
            sourceValue = it
            if (hasReceiverEmitted && hasSourceEmitted) {
                value = zipFunction(receiverValue!!, sourceValue!!)
                hasReceiverEmitted = false
                hasSourceEmitted = false
            }
        }
    }
}
