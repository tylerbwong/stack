package me.tylerbwong.stack.data.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> CoroutineScope.safeResultOf(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        block: suspend () -> T
): Result<T> {
    return try {
        Result.Success(withContext(dispatcher) { block() })
    } catch (ex: Exception) {
        Result.Error(ex)
    }
}
