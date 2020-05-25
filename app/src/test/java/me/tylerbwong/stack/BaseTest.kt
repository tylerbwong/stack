package me.tylerbwong.stack

import android.content.Context
import android.os.Build
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import me.tylerbwong.stack.data.auth.TestSharedPreferencesModule
import me.tylerbwong.stack.ui.ApplicationWrapper
import me.tylerbwong.stack.ui.di.DaggerStackComponent
import me.tylerbwong.stack.ui.di.StackComponent
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

    protected lateinit var stackComponent: StackComponent

    protected lateinit var lifecycleOwner: TestLifecycleOwner

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        ApplicationWrapper.init(context)
        stackComponent = DaggerStackComponent.builder()
            .sharedPreferencesModule(TestSharedPreferencesModule())
            .build()
        lifecycleOwner = TestLifecycleOwner()
        lifecycleOwner.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    @After
    fun tearDownTest() {
        lifecycleOwner.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }
}
