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

@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Flow<T>.retryable(trigger: RetryTrigger): Flow<T> {
    return trigger.retryEvent
        .filter { it == RetryTrigger.State.RETRYING }
        .onEach { trigger.retryEvent.value = RetryTrigger.State.IDLE }
        .flatMapLatest { this }
}
