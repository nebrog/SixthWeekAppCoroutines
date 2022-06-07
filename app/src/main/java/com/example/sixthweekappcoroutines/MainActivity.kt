package com.example.sixthweekappcoroutines

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.sixthweekappcoroutines.domain.CoroutineWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), Callbacks {

    private val handler = Handler(Looper.getMainLooper())
    private val worker: Background = CoroutineWorker(this)

    //    private val piText by lazy { findViewById<TextView>(R.id.text) }
    private val timeText by lazy { findViewById<TextView>(R.id.timer) }
    private val timeBackground by lazy { findViewById<View>(R.id.play_pause) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        worker.create()
        val play = findViewById<Button>(R.id.play)
        val pause = findViewById<Button>(R.id.pause)
        val reset = findViewById<Button>(R.id.reset)
        play.setOnClickListener { worker.play() }
        pause.setOnClickListener { worker.pause() }
        reset.setOnClickListener { worker.reset() }
    }

    override fun onDestroy() {
        super.onDestroy()
        worker.destroy()
    }

    override suspend fun onPiChanged(pi: Double) {

    }

    override suspend fun onTimeChanged(timeMs: Long) {
        withContext(Dispatchers.Main) {
            val minutes = timeMs / 1000 / 60
            val seconds = timeMs / 1000 % 60
            timeText.text = "${minutes.zeroPadding()}:${seconds.zeroPadding()}"
        }

    }

    override suspend fun onColorChanged(color: Int) {
        withContext(Dispatchers.Main) {
            timeBackground.setBackgroundColor(color)
        }

    }

    private fun Long.zeroPadding(): String = "%02d".format(this)
}