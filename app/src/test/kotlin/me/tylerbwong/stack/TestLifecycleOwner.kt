package me.tylerbwong.stack

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

class TestLifecycleOwner : LifecycleOwner {

    private val registry = LifecycleRegistry(this)

    override fun getLifecycle() = registry
}
