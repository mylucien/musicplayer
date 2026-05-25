package com.carmusic.player

import android.app.Application

class CarMusicApp : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: CarMusicApp
            private set
    }
}
