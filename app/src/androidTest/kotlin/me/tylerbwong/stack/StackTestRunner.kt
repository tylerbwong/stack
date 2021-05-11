package me.tylerbwong.stack

import android.os.Bundle
import androidx.test.runner.AndroidJUnitRunner
import coil.Coil
import com.facebook.testing.screenshot.ScreenshotRunner
import me.tylerbwong.stack.utils.TestImageLoader

class StackTestRunner : AndroidJUnitRunner() {

    override fun onCreate(arguments: Bundle?) {
        Coil.setImageLoader(TestImageLoader(context))
        ScreenshotRunner.onCreate(this, arguments)
        super.onCreate(arguments)
    }

    override fun finish(resultCode: Int, results: Bundle?) {
        ScreenshotRunner.onDestroy()
        super.finish(resultCode, results)
    }
}
