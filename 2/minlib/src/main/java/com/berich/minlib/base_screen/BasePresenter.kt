/*
 * // Copyright 2019 Mars Development
 * //
 * // Licensed under the Apache License, Version 2.0 (the "License");
 * // you may not use this file except in compliance with the License.
 * // You may obtain a copy of the License at
 * //
*/

package com.berich.minlib.base_screen

import androidx.lifecycle.Lifecycle
import com.berich.minlib.utils.SoftKeyBoardUtil
import com.ninja.browser.free.video.downloader.base_screen.MvpPresenter
import com.ninja.browser.free.video.downloader.base_screen.MvpRepository
import com.ninja.browser.free.video.downloader.base_screen.MvpView
import com.trello.rxlifecycle3.LifecycleTransformer
import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<T : MvpView, R : MvpRepository>(var view: T, var repository: R) :
	MvpPresenter<T> {

	var compositeDisposable = CompositeDisposable()

	override fun hasBackArrow(): Boolean? = true

	override fun onCreate() {
	}

	override fun onViewCreated() {
		view.initToolbar(view.getTitleText())
	}

	override fun onStart() {
		view.addListeners()
		initSubscriptions()
	}

	override fun onResume() {
	}

	override fun onHiddenChanged(hidden: Boolean) {

	}

	override fun onPause() {
	}

	// Why you need to unregister listeners? See https://stackoverflow.com/q/38368391
	override fun onStop() {
		view.clearListeners()
	}

	override fun onDestroy() {
		compositeDisposable.clear()
	}

	override fun onClose() {
	}

	override fun initSubscriptions() {
		SoftKeyBoardUtil.subKeyboardStatus
				.distinctUntilChanged()
				.compose(bindUntilEvent())
				.subscribe {
					if (it) {
						view.onShowKeyboard()
					} else {
						view.onHideKeyboard()
					}
				}
	}

	open fun <D> bindUntilEvent(event: Lifecycle.Event = Lifecycle.Event.ON_STOP): LifecycleTransformer<D> {
		return view.getLifecycleProvider().bindUntilEvent(event)
	}
}
