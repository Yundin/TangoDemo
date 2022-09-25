package com.yundin.core.utils

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

class RetryTrigger {
    enum class State { RETRYING, IDLE }

    val retryEvent = MutableStateFlow(State.RETRYING)

    fun retry() {
        retryEvent.value = State.RETRYING
    }
}

/**
 * Restarting upstream whenever [RetryTrigger.retry] is called for [trigger].
 * From the downstream perspective previous flow continues.
 *
 * For example:
 *
 * ```
 * flowOf(0)
 *    .onStart { emit(1) }
 *    .retryable(trigger)
 *    .onStart { emit(2) }
 *    .collect { print(it) }
 * ```
 * On first start prints "210" and after each retry prints "10".
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Flow<T>.retryable(trigger: RetryTrigger): Flow<T> {
    return trigger.retryEvent
        .filter { it == RetryTrigger.State.RETRYING }
        .onEach { trigger.retryEvent.value = RetryTrigger.State.IDLE }
        .flatMapLatest { this }
}
