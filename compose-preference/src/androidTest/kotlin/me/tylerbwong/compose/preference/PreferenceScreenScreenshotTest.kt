package me.tylerbwong.compose.preference

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.platform.app.InstrumentationRegistry
import com.facebook.testing.screenshot.Screenshot
import com.facebook.testing.screenshot.ViewHelpers
import org.junit.Rule
import org.junit.Test

class PreferenceScreenScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val targetContext: Context
        get() = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun capturePreferenceScreen() {
        composeTestRule.setContent {
            PreferenceScreenPreview()
        }
        composeTestRule.onRoot().capture()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun SemanticsNodeInteraction.capture() {
        val bitmap = captureToImage().asAndroidBitmap()
        val imageView = ImageView(targetContext).apply {
            setImageBitmap(bitmap)
        }
        val displayMetrics = DisplayMetrics()
        val display = targetContext.display
        val viewHelpers = ViewHelpers.setupView(imageView)
        if (display != null) {
            display.getRealMetrics(displayMetrics)
            viewHelpers.setExactWidthPx(displayMetrics.widthPixels)
        }
        viewHelpers.layout()
        Screenshot.snap(imageView).record()
    }
}
