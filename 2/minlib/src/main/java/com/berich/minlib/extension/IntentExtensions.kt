package com.berich.minlib.extension

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.fragment.app.Fragment

inline fun Context.setting() = startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply { data = Uri.parse("package:${packageName}") })
inline fun Fragment.setting() = context?.setting()

inline fun Context.startActivityR(intent: Intent): Boolean {
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
        return true
    }
    return false
}