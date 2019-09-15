package me.tylerbwong.stack

import android.content.Context
import android.os.Build
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import org.junit.After
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

    protected lateinit var lifecycleOwner: TestLifecycleOwner

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        lifecycleOwner = TestLifecycleOwner()
        lifecycleOwner.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    @After
    fun tearDownTest() {
        lifecycleOwner.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }
}
