package me.tylerbwong.stack.data.persistence.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListTypeConverter {

    private val gson by lazy { Gson() }

    @TypeConverter
    fun stringListToJson(stringList: List<String>?): String? = stringList?.let { gson.toJson(it) }

    @TypeConverter
    fun jsonToStringList(json: String?): List<String>? = json?.let {
        with(object : TypeToken<List<String>>() {}.type) {
            gson.fromJson(it, this)
        }
    }
}
