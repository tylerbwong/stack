package me.tylerbwong.compose.preference

import android.content.SharedPreferences
import androidx.compose.runtime.staticAmbientOf

val PreferenceAmbient = staticAmbientOf<SharedPreferences>()
