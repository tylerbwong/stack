package me.tylerbwong.stack.screenshot

import com.facebook.testing.screenshot.Screenshot
import me.tylerbwong.stack.integration.BaseIntegrationTest
import me.tylerbwong.stack.ui.settings.libraries.LibrariesActivity
import org.junit.Test

class LibrariesActivityScreenshotTest : BaseIntegrationTest<LibrariesActivity>(
    LibrariesActivity::class.java
) {
    @Test
    fun captureLibrariesActivity() {
        waitForRequest()
        Screenshot.snapActivity(activity).record()
    }
}
