package com.example.sixthweekappcoroutines.domain

import android.graphics.Color
import android.util.Log
import com.example.sixthweekappcoroutines.Background
import com.example.sixthweekappcoroutines.Background.Companion.COLOR_TIMEOUT
import com.example.sixthweekappcoroutines.Background.Companion.TIME_SLEEP_PAUSE
import com.example.sixthweekappcoroutines.Background.Companion.TIME_TIMEOUT
import com.example.sixthweekappcoroutines.Background.Companion.colors
import com.example.sixthweekappcoroutines.Callbacks
import kotlinx.coroutines.*
import java.util.*
import kotlin.system.measureTimeMillis

class CoroutineWorker(private val uiCallback: Callbacks) : Background {

    private val scope = CoroutineScope(Dispatchers.IO)

    @Volatile
    private var isPaused = false
    private val random = Random(System.currentTimeMillis())

    @Volatile
    private var sleepTime = 0L

    @Volatile
    private var timeMillis = 0L

    @Volatile
    private var n = 1


    override fun create() {


        scope.launch {
            while (true) {
                while (sleepTime < COLOR_TIMEOUT) {
                    delay(TIME_SLEEP_PAUSE)
                    if (!isPaused) {
                        sleepTime += TIME_SLEEP_PAUSE
                    }
                }
                val colorIndex = random.nextInt(colors.size)
                val color = colors.get(colorIndex)
                uiCallback.onColorChanged(color)
                sleepTime = 0
            }
        }

        scope.launch {
            while (true) {
                while (isPaused) {
                    delay(TIME_SLEEP_PAUSE)
                }
                delay(TIME_TIMEOUT)
                timeMillis += TIME_TIMEOUT
                if (!isPaused) {
                    val measure = measureTimeMillis {
                        uiCallback.onTimeChanged(timeMillis)
                    }
                    Log.e("Nebrog", measure.toString())
                    timeMillis += measure
                }
            }
        }
        scope.launch(Dispatchers.Default) {
            while (true) {
                while (isPaused) {
                    delay(TIME_SLEEP_PAUSE)
                }
                val piNumber = GenerationPI.formula(n)
                val piString = "%.${2 * n}f".format(piNumber)
                val pi = piString.subSequence(0, n + 2)
                uiCallback.onPiChanged(pi)
                n++
            }
        }
    }


    override fun destroy() {
        scope.cancel()
    }

    override fun pause() {
        isPaused = true
    }

    override fun play() {
        isPaused = false
    }

    override fun reset() {
        sleepTime = 0
        timeMillis = 0
        isPaused = false
        n = 1
        scope.launch { uiCallback.onColorChanged(Color.WHITE) }

    }
}