package com.opengles_demo

import android.app.Application
import android.content.Context

class GlApplication : Application() {
    companion object{
        lateinit var context : Context
    }
    override fun onCreate() {
        super.onCreate()
        context = this
    }
}