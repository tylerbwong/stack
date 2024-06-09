package me.tylerbwong.stack.data.persistence.typeconverter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

@ProvidedTypeConverter
class ListTypeConverter(private val json: Json) {

    private val listStringType = ListSerializer(String.serializer())

    @TypeConverter
    fun jsonToStringList(value: String?): List<String>? = value?.let {
        json.decodeFromString(listStringType, it)
    }

    @TypeConverter
    fun stringListToJson(listValue: List<String>?): String? = listValue?.let {
        json.encodeToString(listStringType, it)
    }
}
