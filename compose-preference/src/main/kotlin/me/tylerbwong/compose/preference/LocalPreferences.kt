package me.tylerbwong.compose.preference

import androidx.compose.runtime.staticCompositionLocalOf
import com.tfcporciuncula.flow.FlowSharedPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
internal val LocalPreferences = staticCompositionLocalOf<FlowSharedPreferences> {
    error("No preferences provided")
}
