package me.tylerbwong.stack.utils

import android.content.Context
import androidx.annotation.RawRes
import com.squareup.moshi.Moshi
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.Charset

inline fun <reified T> Context.rawResourceOfType(@RawRes resource: Int): T {
    val inputStream = resources.openRawResource(resource)
    val bufferedReader = BufferedReader(InputStreamReader(inputStream, Charset.defaultCharset()))
    val resourceString = bufferedReader.use { it.readText() }
    return Moshi.Builder().build().adapter(T::class.java).fromJson(resourceString)!!
}
