package com.example.myactivitiyes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.*

class AppViewModel : ViewModel() {
    val counterValue: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}
class MainActivity : AppCompatActivity() {
    private lateinit var counterTextView: TextView
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var nextActivityButton: Button
    private lateinit var model: AppViewModel
    companion object {
        private var ACTIVITY_COUNTER = 0
        private var IS_TIMER_RUNNING = false
        private var ACTIVITY_1: MainActivity? = null
        private var JOB: Job? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        counterTextView = findViewById(R.id.counter)
        startButton = findViewById(R.id.startButton)
        stopButton = findViewById(R.id.stopButton)
        nextActivityButton = findViewById(R.id.button2)
        model = ViewModelProvider(this)[AppViewModel::class.java]
        startButton.setOnClickListener {
            startTimer()
        }
        stopButton.setOnClickListener {
            stopTimer()
        }
        nextActivityButton.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }
        model.counterValue.observe(this) { newVal ->
            counterTextView.text = newVal
        }
        ACTIVITY_1 = this
        counterTextView.text = ACTIVITY_COUNTER.toString()
    }

    override fun onPause() {
        super.onPause()
        ACTIVITY_1 = null
    }
    private fun startTimer() {
        if (!IS_TIMER_RUNNING) {
            JOB = CoroutineScope(Dispatchers.Main).launch {
                while (isActive) {
                    delay(1000)
                    ACTIVITY_COUNTER++
                    ACTIVITY_1?.model?.counterValue?.value = ACTIVITY_COUNTER.toString()
                }
            }
            IS_TIMER_RUNNING = true
        }
    }
    private fun stopTimer() {
        JOB?.cancel()
        IS_TIMER_RUNNING = false
    }
}
