package me.tylerbwong.stack.ui.utils

import androidx.lifecycle.MutableLiveData
import me.tylerbwong.stack.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

class LiveDataTest : BaseTest() {

    private lateinit var receiver: MutableLiveData<Receiver>
    private lateinit var source: MutableLiveData<Source>

    @Before
    fun setUp() {
        receiver = MutableLiveData()
        source = MutableLiveData()
    }

    @Test
    fun `zipWith with initial value emits passed in value`() {
        val testReceiverValue = Receiver(10)
        val zipped = receiver.zipWith(source, initialValue = testReceiverValue) { receiver, _ ->
            receiver
        }
        assertEquals(testReceiverValue, zipped.value)
    }

    @Test
    fun `zipWith does not emit when only receiver emits`() {
        val testReceiverValue = Receiver(10)
        val zipped = receiver.zipWith(source) { _, _ ->
            fail("zipWith returned with only one receiver emission")
        }
        zipped.observe(lifecycleOwner) {
            fail("zipWith returned with only one receiver emission")
        }
        receiver.value = testReceiverValue
    }

    @Test
    fun `zipWith does not emit when only source emits`() {
        val testSourceValue = Source(10)
        val zipped = receiver.zipWith(source) { _, _ ->
            fail("zipWith returned with only one source emission")
        }
        zipped.observe(lifecycleOwner) {
            fail("zipWith returned with only one source emission")
        }
        source.value = testSourceValue
    }

    @Test
    fun `zipWith emits when both source and receiver emit`() {
        val testReceiverValue = Receiver(15)
        val testSourceValue = Source(10)
        val zipped = receiver.zipWith(source) { receiver, source -> receiver.count + source.count }
        zipped.observe(lifecycleOwner) {
            assertEquals(25, it)
        }
        receiver.value = testReceiverValue
        source.value = testSourceValue
    }

    private data class Receiver(val count: Int = 0)
    private data class Source(val count: Int = 0)
}
