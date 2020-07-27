package com.berich.minlib.extension

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.fragment.app.Fragment

inline fun Context.copy(message: String, tag: String = "COPY") = (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).apply { setPrimaryClip(ClipData.newPlainText(tag, message)) }
inline fun Fragment.copy(message: String, tag: String = "COPY") = (context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).apply { setPrimaryClip(ClipData.newPlainText(tag, message)) }

inline fun Context.paste(): String = (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).run {
    if (this.hasPrimaryClip()) {
        return@run this.primaryClip!!.getItemAt(0).text.toString()
    } else return@run ""
}

inline fun Fragment.paste(): String = context?.paste() ?: ""