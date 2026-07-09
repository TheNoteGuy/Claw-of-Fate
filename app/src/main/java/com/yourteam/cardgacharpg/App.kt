package com.yourteam.cardgacharpg

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Application class — aktiviert Hilt für den ganzen App-Graph.
// Muss in AndroidManifest.xml als android:name=".App" referenziert sein (ist es bereits).
@HiltAndroidApp
class App : Application()