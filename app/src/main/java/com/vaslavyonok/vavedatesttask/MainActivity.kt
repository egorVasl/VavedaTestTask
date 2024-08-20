package com.vaslavyonok.vavedatesttask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vaslavyonok.vavedatesttask.ui.theme.VavedatesttaskTheme
import com.vaslavyonok.vavedatesttask.utils.TimerObserver
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var timerObserver: TimerObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(timerObserver)

        enableEdgeToEdge()
        setContent {
            VavedatesttaskTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val time by timerObserver.timerUseCase.timerStateFlow.collectAsStateWithLifecycle()
                    Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                        Text(
                            text = "All PLay Time: " + time.displaySeconds,
                            fontSize = 24.sp,
                            fontWeight = Medium,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}