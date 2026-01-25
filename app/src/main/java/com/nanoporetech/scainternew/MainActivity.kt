package com.nanoporetech.scainternew

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.nanoporetech.scainternew.screens.App
import com.nanoporetech.scainternew.ui.theme.ScaInterNewTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScaInterNewTheme {
                val windowSize = calculateWindowSizeClass(this)

                App(
                    windowSize = windowSize.widthSizeClass
                )
            }
        }
    }
}
