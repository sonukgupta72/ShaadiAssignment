package com.sonukgupta72.shaadiassignment

import android.app.Application
import android.content.Context

class MyApplication: Application() {

    companion object{
        var appContext: Context? = null
    }

    override fun onCreate() {
        appContext = this
        super.onCreate()
    }
}