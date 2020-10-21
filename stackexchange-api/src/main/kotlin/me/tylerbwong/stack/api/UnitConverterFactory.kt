package me.tylerbwong.stack.api

import me.tylerbwong.stack.api.model.Response
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

internal object UnitConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, Response<Unit>>? = if (type == Unit::class.java) {
        UnitConverter
    } else {
        null
    }

    private object UnitConverter : Converter<ResponseBody, Response<Unit>> {
        override fun convert(value: ResponseBody): Response<Unit> {
            value.close()
            return Response.EMPTY
        }
    }
}
