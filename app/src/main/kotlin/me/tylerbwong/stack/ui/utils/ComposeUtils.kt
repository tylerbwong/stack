package me.tylerbwong.stack.ui.utils

import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.PluralsRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ContextAmbient

@Composable
fun colorAttribute(@AttrRes id: Int): Color {
    val typedValue = TypedValue()
    val context = ContextAmbient.current
    context.theme.resolveAttribute(id, typedValue, true)
    val arr = context.obtainStyledAttributes(typedValue.data, intArrayOf(id))
    val color = arr.getColor(0, -1)
    arr.recycle()
    return Color(color)
}

@Composable
fun quantityResource(
    @PluralsRes id: Int,
    quantity: Int
): String = ContextAmbient.current.resources.getQuantityString(id, quantity, quantity)
