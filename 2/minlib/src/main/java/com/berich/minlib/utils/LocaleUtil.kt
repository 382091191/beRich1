/*
 * // Copyright 2019 Mars Development
 * //
 * // Licensed under the Apache License, Version 2.0 (the "License");
 * // you may not use this file except in compliance with the License.
 * // You may obtain a copy of the License at
 * //
*/

package com.berich.minlib.utils

import android.content.Context
import android.os.Build
import android.os.LocaleList
import java.util.*

object LocaleUtil {

    open val supportedLanguages = mapOf(
            "en" to SupportedLanguage("en", "English", "English"),
            "zh" to SupportedLanguage("zh", "Chinese", "中文"))

    private var crtLanguageCode = supportedLanguages["en"]!!.languageCode

    fun getCurrentLanguage(): SupportedLanguage {
        return supportedLanguages[crtLanguageCode]!!
    }

    fun loadLocale(language: String) {
        crtLanguageCode = (supportedLanguages[language]
                ?: supportedLanguages[Locale.getDefault().language]
                ?: supportedLanguages["en"]!!).languageCode
    }

    fun selectLanguage(language: SupportedLanguage) {
        crtLanguageCode = language.languageCode
    }

    object ContextWrapper {
        fun wrap(context: Context?): android.content.ContextWrapper {
            var conf = context?.resources?.configuration

            TimeUtil.locale = Locale(crtLanguageCode)
            conf?.setLocale(Locale(crtLanguageCode))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                conf?.locales=LocaleList(Locale(crtLanguageCode)).apply { LocaleList.setDefault(this) }
            }

            Locale.setDefault(Locale(crtLanguageCode))

            val newContext = if (conf == null) null else context?.createConfigurationContext(conf)
            return android.content.ContextWrapper(newContext)
        }
    }

    data class SupportedLanguage(val languageCode: String, val englishName: String, val nativeName: String)
}