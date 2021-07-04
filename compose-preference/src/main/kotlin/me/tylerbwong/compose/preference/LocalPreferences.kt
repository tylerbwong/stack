package me.tylerbwong.compose.preference

import androidx.compose.runtime.staticCompositionLocalOf
import com.fredporciuncula.flow.preferences.FlowSharedPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
internal val LocalPreferences = staticCompositionLocalOf<FlowSharedPreferences> {
    error("No preferences provided")
}
