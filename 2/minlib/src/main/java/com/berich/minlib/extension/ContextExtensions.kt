package com.berich.minlib.extension

import android.content.Context
import android.provider.Settings

//判断是否开启了自动亮度调节
fun Context.isAutoBrightness(): Boolean {
    var isAutoAdjustBright = false
    try {
        isAutoAdjustBright = (Settings.System
                .getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE)
                == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC)
    } catch (e: Settings.SettingNotFoundException) {
        e.printStackTrace()
    }
    return isAutoAdjustBright
}

//关闭亮度自动调节
fun Context.stopAutoBrightness() {
    Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE,
            Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL)
}

//开启亮度自动调节
fun Context.startAutoBrightness() {
    Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE,
            Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC)
}