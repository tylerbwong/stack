package me.tylerbwong.stack.data.persistence.typeconverter

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.tylerbwong.stack.ui.ApplicationWrapper

class ListTypeConverter {

    private val moshi: Moshi
        get() = EntryPoints.get(ApplicationWrapper.context, MoshiEntryPoint::class.java).moshi()

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

@EntryPoint
@InstallIn(SingletonComponent::class)
interface MoshiEntryPoint {
    fun moshi(): Moshi
}
