package me.tylerbwong.compose.preference

import androidx.compose.runtime.staticCompositionLocalOf
import com.tfcporciuncula.flow.FlowSharedPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
internal val LocalPreferences = staticCompositionLocalOf<FlowSharedPreferences> {
    error("No preferences provided")
}
