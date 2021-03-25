package me.tylerbwong.compose.preference

import android.content.SharedPreferences
import androidx.compose.runtime.staticCompositionLocalOf

val LocalPreferences = staticCompositionLocalOf<SharedPreferences> {
    error("No preferences provided")
}
