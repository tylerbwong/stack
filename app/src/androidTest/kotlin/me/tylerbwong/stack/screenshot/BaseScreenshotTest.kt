package me.tylerbwong.stack.screenshot

import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.view.ContextThemeWrapper
import androidx.test.platform.app.InstrumentationRegistry
import com.facebook.testing.screenshot.Screenshot
import com.facebook.testing.screenshot.ViewHelpers
import me.tylerbwong.stack.R
import org.junit.Before

abstract class BaseScreenshotTest {

    protected lateinit var container: FrameLayout

    protected val targetContext: Context
        get() = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setUp() {
        container = FrameLayout(ContextThemeWrapper(targetContext, R.style.AppTheme_Base_Primary))
    }

    protected fun View.capture() {
        val displayMetrics = DisplayMetrics()
        val display = targetContext.display
        val viewHelpers = ViewHelpers.setupView(this)
        if (display != null) {
            display.getRealMetrics(displayMetrics)
            viewHelpers.setExactWidthPx(displayMetrics.widthPixels)
        }
        viewHelpers.layout()
        Screenshot.snap(this).record()
    }
}
