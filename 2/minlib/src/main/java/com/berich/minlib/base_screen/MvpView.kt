/*
 * // Copyright 2019 Mars Development
 * //
 * // Licensed under the Apache License, Version 2.0 (the "License");
 * // you may not use this file except in compliance with the License.
 * // You may obtain a copy of the License at
 * //
*/

package com.ninja.browser.free.video.downloader.base_screen

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.berich.minlib.base_screen.BasePresenter
import com.trello.rxlifecycle3.LifecycleProvider

interface MvpView {
    fun initPresenter(): BasePresenter<out MvpView, out MvpRepository>?
    fun initToolbar(title: String?)
    fun getTitleText(): String?

    fun addListeners()
    fun clearListeners()
    fun onHideKeyboard()
    fun onShowKeyboard()

    fun getContext(): Context?
    fun getLifecycleOwner(): LifecycleOwner
    fun getLifecycleProvider(): LifecycleProvider<Lifecycle.Event>

    fun toast(tip: String)
}
