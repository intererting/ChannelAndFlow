package com.yly.channelandflow

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * @author    yiliyang
 * @date      21-1-7 下午2:11
 * @version   1.0
 * @since     1.0
 */
class EventBus {
    private val _events = MutableSharedFlow<String>(1) // private mutable shared flow
    val events = _events.asSharedFlow() // publicly exposed as read-only shared flow

    suspend fun produceEvent(event: String) {
        _events.emit(event) // suspends until all subscribers receive it
    }
}