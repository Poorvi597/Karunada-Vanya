package com.karunadavanya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import com.karunadavanya.presentation.KarunadaVanyaApp
import com.karunadavanya.presentation.theme.KarunadaVanyaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val container = (application as KarunadaVanyaApplication).container
        setContent {
            KarunadaVanyaTheme {
                KarunadaVanyaApp(container = remember { container })
            }
        }
    }
}
