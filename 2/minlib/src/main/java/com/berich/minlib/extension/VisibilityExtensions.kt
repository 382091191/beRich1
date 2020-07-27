/*
 * // Copyright 2019 Mars Development
 * //
 * // Licensed under the Apache License, Version 2.0 (the "License");
 * // you may not use this file except in compliance with the License.
 * // You may obtain a copy of the License at
 * //
*/

package com.berich.minlib.extension

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View

fun View.display(control: Int, animate: Boolean = false) {
    if (animate) {
        animate().alpha(0f).setDuration(300).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                visibility = control
            }
        })
    } else {
        visibility = control
    }
}

inline fun View.visible(animate: Boolean = false) {
    display(View.VISIBLE, animate)
}

inline fun View.hide(hidingStrategy: Int, animate: Boolean = false) {
    display(hidingStrategy, animate)
}

/** Set the View visibility to INVISIBLE and eventually animate view alpha till 0% */
inline fun View.invisible(animate: Boolean = false) {
    hide(View.INVISIBLE, animate)
}

/** Set the View visibility to GONE and eventually animate view alpha till 0% */
inline fun View.gone(animate: Boolean = false) {
    hide(View.GONE, animate)
}

/** Convenient method that chooses between View.visible() or View.invisible() methods */
inline fun View.visibleOrInvisible(show: Boolean, animate: Boolean = false) {
    if (show) visible(animate) else invisible(animate)
}

/** Convenient method that chooses between View.visible() or View.gone() methods */
inline fun View.visibleOrGone(show: Boolean, animate: Boolean = false) {
    if (show) visible(animate) else gone(animate)
}

/** Convenient method that chooses between View.invisible() or View.gone() methods */
inline fun View.invisibleOrGone(show: Boolean, animate: Boolean = false) {
    if (show) invisible(animate) else gone(animate)
}