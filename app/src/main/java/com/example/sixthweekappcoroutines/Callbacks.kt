package com.example.sixthweekappcoroutines

import androidx.annotation.ColorInt

interface Callbacks {
    suspend fun onPiChanged(pi: Double)
    suspend fun onTimeChanged(timeMs: Long)
    suspend fun onColorChanged(@ColorInt color: Int)
}