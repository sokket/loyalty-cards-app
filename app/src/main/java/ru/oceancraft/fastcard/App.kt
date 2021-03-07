package ru.oceancraft.fastcard

import android.app.Application

class App : Application() {

    override fun onCreate() {
        Dagger.init(this)
        super.onCreate()
    }

}