package me.tylerbwong.stack.data.persistence.typeconverter

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import javax.inject.Inject

class ListTypeConverter {

    @Inject
    lateinit var moshi: Moshi

    @TypeConverter
    fun stringListToJson(stringList: List<String>?): String? = stringList?.let {
        moshi.adapter(List::class.java).toJson(it)
    }

    @TypeConverter
    fun jsonToStringList(json: String?): List<String>? = json?.let {
        val type = Types.newParameterizedType(List::class.java, String::class.java)
        moshi.adapter<List<String>>(type).fromJson(it)
    }
}
