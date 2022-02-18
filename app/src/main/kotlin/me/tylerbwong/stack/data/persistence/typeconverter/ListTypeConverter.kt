package me.tylerbwong.stack.data.persistence.typeconverter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

@ProvidedTypeConverter
class ListTypeConverter(moshi: Moshi) {

    private val listStringType = Types.newParameterizedType(List::class.java, String::class.java)
    private val stringListAdapter = moshi.adapter<List<String>>(listStringType)
    private val listAdapter = moshi.adapter(List::class.java)

    @TypeConverter
    fun jsonToStringList(json: String?): List<String>? = json?.let {
        stringListAdapter.fromJson(it)
    }

    @TypeConverter
    fun stringListToJson(stringList: List<String>?): String? = stringList?.let {
        listAdapter.toJson(it)
    }
}
