package com.vaslavyonok.vavedatesttask.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import javax.inject.Inject

class TimerUseCase @Inject constructor(
    context: Context
) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

    private var job: Job? = null

    private var timerState = TimerState(
        accumulatedSeconds = prefs.getInt(ACCUMULATED_SECONDS_PREF_KEY, 0)
    )

    private val _timerStateFlow = MutableStateFlow(timerState)
    val timerStateFlow: StateFlow<TimerState> = _timerStateFlow

    private fun saveAccumulatedTime() {
        prefs.edit()
            .putInt(ACCUMULATED_SECONDS_PREF_KEY, timerState.accumulatedSeconds)
            .apply()
    }

    fun toggleStartTimer(scope: CoroutineScope) {
        if (job == null) {
            job = scope.launch {
                initTimer()
                    .onCompletion {
                        saveAccumulatedTime()
                        job = null
                    }
                    .collect { newState ->
                        _timerStateFlow.emit(newState)
                        timerState = newState
                        saveAccumulatedTime()
                    }
            }
        }
    }

    fun toggleStopTimer() {
        if (job != null) {
            job?.cancel()
            job = null
            saveAccumulatedTime()
        }
    }

    private fun initTimer(): Flow<TimerState> = flow {
        while (true) {
            emit(timerState.copy(accumulatedSeconds = timerState.accumulatedSeconds + 1))
            delay(1000)
        }
    }
}

data class TimerState(
    val accumulatedSeconds: Int = 0,
    val isFinished: Boolean = false
) {
    val displaySeconds: String = displayTime(accumulatedSeconds)

    @SuppressLint("DefaultLocale")
    private fun displayTime(time: Int): String {
        val hours = time / 3600
        val minutes = time % 3600 / 60
        val seconds = time % 60
        return String.format(DISPLAY_TIME_FORMAT, hours, minutes, seconds)
    }
}
