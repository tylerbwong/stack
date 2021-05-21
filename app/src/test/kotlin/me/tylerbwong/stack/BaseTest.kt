package me.tylerbwong.stack

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Suppress("UnnecessaryAbstractClass")
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P], application = TestApplication::class)
abstract class BaseTest {
    protected val context: Context
        get() = ApplicationProvider.getApplicationContext()

    protected lateinit var lifecycleOwner: TestLifecycleOwner
    protected lateinit var testSharedPreferences: SharedPreferences

    private lateinit var mockCloseable: AutoCloseable

    @Before
    fun setUpTest() {
        mockCloseable = MockitoAnnotations.openMocks(this)
        lifecycleOwner = TestLifecycleOwner()
        lifecycleOwner.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        testSharedPreferences = context.getSharedPreferences(
            TEST_SHARED_PREFS,
            Context.MODE_PRIVATE
        )
    }

    @After
    fun tearDownTest() {
        mockCloseable.close()
        lifecycleOwner.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }

    companion object {
        private const val TEST_SHARED_PREFS = "test_preferences"
    }
}
