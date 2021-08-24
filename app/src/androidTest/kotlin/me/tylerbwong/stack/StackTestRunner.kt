package me.tylerbwong.stack

import android.os.Bundle
import androidx.test.runner.AndroidJUnitRunner
import coil.Coil
import me.tylerbwong.stack.utils.TestImageLoader

class StackTestRunner : AndroidJUnitRunner() {
    override fun onCreate(arguments: Bundle?) {
        Coil.setImageLoader(TestImageLoader(context))
        super.onCreate(arguments)
    }
}
