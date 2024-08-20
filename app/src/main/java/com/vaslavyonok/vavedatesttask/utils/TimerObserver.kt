package com.vaslavyonok.vavedatesttask.utils

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject

class TimerObserver @Inject constructor(
    val timerUseCase: TimerUseCase,
) : DefaultLifecycleObserver {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        timerUseCase.toggleStartTimer(scope)
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        timerUseCase.toggleStopTimer()
    }
}