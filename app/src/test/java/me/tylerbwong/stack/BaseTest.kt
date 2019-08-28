package me.tylerbwong.stack

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
abstract class BaseTest {
    protected val context: Context
        get() = ApplicationProvider.getApplicationContext()

    @Before
    fun initMocks() {
        MockitoAnnotations.initMocks(this)
    }
}
