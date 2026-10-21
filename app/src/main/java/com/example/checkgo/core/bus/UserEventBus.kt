package com.example.checkgo.core.bus

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

sealed class UserEvent {
    object UserCreated : UserEvent()
}

@Singleton
class UserEventBus @Inject constructor() {
    private val _events = MutableSharedFlow<UserEvent>(replay = 0)
    val events = _events.asSharedFlow()

    suspend fun notifyUserCreated() {
        _events.emit(UserEvent.UserCreated)
    }
}