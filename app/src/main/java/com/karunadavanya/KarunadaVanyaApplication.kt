package com.karunadavanya

import android.app.Application
import com.karunadavanya.data.AppContainer
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class KarunadaVanyaApplication : Application() {
    val container: AppContainer by lazy { AppContainer(this) }
}
