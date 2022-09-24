package com.yundin.core.utils

import java.util.concurrent.CancellationException

sealed class Result<out S, out E> {

    data class Success<out S>(val result: S) : Result<S, Nothing>()

    data class Error<out E>(val result: E) : Result<Nothing, E>()
}

inline fun <S, R> S.runBlockCatching(block: S.() -> R): Result<R, Throwable> {
    return try {
        Result.Success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Result.Error(e)
    }
}
