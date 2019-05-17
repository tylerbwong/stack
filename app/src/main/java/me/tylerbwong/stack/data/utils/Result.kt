package me.tylerbwong.stack.data.utils

sealed class Result<T> {
    class Success<T>(val data: T) : Result<T>()
    class Error<T>(val throwable: Throwable) : Result<T>()
}
