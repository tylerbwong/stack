package me.tylerbwong.stack.ui.utils

import android.content.Context
import coil.Coil
import coil.ImageLoader

object CoilInitializer {

    fun init(context: Context) {
        val applicationContext = context.applicationContext
        Coil.setDefaultImageLoader(ImageLoader(applicationContext))
    }
}
