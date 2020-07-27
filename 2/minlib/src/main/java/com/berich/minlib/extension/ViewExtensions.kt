package com.berich.minlib.extension

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.view.View
import android.view.WindowManager

fun View.rotate(from: Float = 0f, duration: Long = 10.second(), repeatCount: Int = 10): ObjectAnimator {
    val angleTo = from + 360f * repeatCount
    val anim = ObjectAnimator.ofFloat(this, "rotation", from, angleTo)
    anim.duration = duration
    return anim
}

fun String.toBitmap(textWidth: Int, textSize: Float, textColor: Int): Bitmap { // Get text dimensions
    val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
            .apply {
                style = Paint.Style.FILL
                color = textColor
                setTextSize(textSize)
            }
    val textLayout = StaticLayout(this, textPaint, textWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false)
    // Create bitmap and canvas to draw to
    val bmp = Bitmap.createBitmap(textWidth, textLayout.height, Bitmap.Config.ARGB_8888)
    val c = Canvas(bmp)
    // Draw background
    val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
    paint.color = Color.TRANSPARENT
    c.drawPaint(paint)
    // Draw text
    c.save()
    textLayout.draw(c)
    c.restore()
    return bmp
}

fun Context.setBackgroundAlpha(alpha: Float) {
    val lp = (this as Activity).window.attributes
    lp.alpha = alpha
    if (alpha == 1f) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND) //不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
    } else {
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND) //此行代码主要是解决在华为手机上半透明效果无效的bug
    }
    window.attributes = lp
}