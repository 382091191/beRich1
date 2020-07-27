package com.berich.minlib

import android.annotation.SuppressLint
import android.app.Application
import android.os.Handler
import android.util.Log

object App {

    private const val TAG = "App"

    private val sApplication: Application by lazy {
        digApplication()
    }
    private val sHandler: Handler by lazy {
        Handler(getApplication().mainLooper)
    }

    fun getMainHandler(): Handler {
        return sHandler
    }

    fun getApplication(): Application {
        return sApplication
    }

    @SuppressLint("PrivateApi")
    private fun digApplication(): Application {
        var app: Application? = null
        try {
            app = Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null) as Application
            checkNotNull(app) { "Static initialization of Applications must be on main thread." }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get current application from AppGlobals." + e.message)
            try {
                app = Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null) as Application
            } catch (ex: Exception) {
                Log.e(TAG, "Failed to get current application from ActivityThread." + e.message)
            }
        }
        return app!!
    }
}