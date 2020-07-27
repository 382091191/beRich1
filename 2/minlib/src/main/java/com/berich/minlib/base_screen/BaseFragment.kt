/*
 * // Copyright 2019 Mars Development
 * //
 * // Licensed under the Apache License, Version 2.0 (the "License");
 * // you may not use this file except in compliance with the License.
 * // You may obtain a copy of the License at
 * //
*/

package com.berich.minlib.base_screen

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.berich.minlib.R
import com.gyf.immersionbar.components.SimpleImmersionOwner
import com.gyf.immersionbar.components.SimpleImmersionProxy
import com.gyf.immersionbar.ktx.immersionBar
import com.ninja.browser.free.video.downloader.base_screen.MvpRepository
import com.ninja.browser.free.video.downloader.base_screen.MvpView
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle
import com.trello.rxlifecycle3.LifecycleProvider
import com.x.common.utils.SoftKeyBoardUtil
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment<T : BasePresenter<out MvpView, out MvpRepository>>
    : Fragment(), MvpView, SimpleImmersionOwner {

    private var immersionProxy: SimpleImmersionProxy = SimpleImmersionProxy(this)
    var compositeDisposable = CompositeDisposable()

    open var onBackPressed: (() -> Unit)? = null
    protected var presenter: T? = null
        private set

    private lateinit var provider: LifecycleProvider<Lifecycle.Event>

    private var rootView: View? = null

    protected val onBackPressedCallback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBackPressed?.invoke()
                }
            }

    protected abstract fun onControllerGetContentLayoutId(): Int
    override fun getTitleText(): String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = initPresenter() as? T
        presenter?.onCreate()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        if (null == rootView) {
            rootView = inflater.inflate(onControllerGetContentLayoutId(), container, false)
        }
        return rootView
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        immersionProxy.onConfigurationChanged(newConfig)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        provider = AndroidLifecycle.createLifecycleProvider(getLifecycleOwner())
        if (onBackPressed != null) {
            requireActivity().onBackPressedDispatcher.addCallback(activity!!, onBackPressedCallback)
        }
        presenter?.onViewCreated()
        initView(view, savedInstanceState)
    }

    abstract fun initView(view: View, savedInstanceState: Bundle?)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        immersionProxy.onActivityCreated(savedInstanceState)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        immersionProxy.isUserVisibleHint = isVisibleToUser
    }

    override fun onStart() {
        super.onStart()
        if (onBackPressed != null) {
            onBackPressedCallback.isEnabled = true
        }
        initImmersionBar()
        presenter?.onStart()
    }

    override fun onResume() {
        super.onResume()
//        activity?.window?.statusBarColor = getStatusBarColor()
        presenter?.onResume()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        immersionProxy.onHiddenChanged(hidden)
        presenter?.onHiddenChanged(hidden)
    }

    override fun onPause() {
        presenter?.onPause()
        SoftKeyBoardUtil.hideSoftKeyboard(context as Activity)
        super.onPause()
    }

    override fun onStop() {
        if (onBackPressed != null) {
            onBackPressedCallback.isEnabled = false
        }
        presenter?.onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        if (onBackPressed != null) {
            onBackPressedCallback.isEnabled = false
            onBackPressedCallback.remove()
        }
        super.onDestroyView()
    }

    override fun onDestroy() {
        presenter?.onDestroy()
        presenter = null
        immersionProxy.onDestroy()
        compositeDisposable.clear()
        super.onDestroy()
    }

    @Nullable
    fun <T : View> findViewById(id: Int): T? {
        return if (view == null) null else view?.findViewById(id)
    }

    override fun getLifecycleOwner(): LifecycleOwner = this
    override fun getLifecycleProvider(): LifecycleProvider<Lifecycle.Event> = provider

    override fun onHideKeyboard() {
    }

    override fun onShowKeyboard() {
    }

    override fun initToolbar(title: String?) {
    }


    override fun addListeners() {
    }

    override fun clearListeners() {
    }

    override fun immersionBarEnabled(): Boolean {
        return true
    }

    override fun toast(tip: String) {
        Toast.makeText(activity, tip, Toast.LENGTH_SHORT).show()
    }

//    override fun onInvisible() {
//
//    }
//
//    override fun onVisible() {
//
//    }
//
//    /**
//     * 懒加载，在view初始化完成之前执行
//     * On lazy after view.
//     */
//    override fun onLazyAfterView() {
//    }
//
//    /**
//     * 懒加载，在view初始化完成之前执行
//     * On lazy after view.
//     */
//    override fun onLazyBeforeView() {
//    }

    override fun initImmersionBar() {
        immersionBar {
            statusBarDarkFont(true, 0.2f)
            statusBarColor(R.color.primary)
            fitsSystemWindows(true)
        }
    }

//    override fun initImmersionBar() {
//        super.initImmersionBar()
//        ImmersionBar.with(this).statusBarColorTransformEnable(false)
//                .keyboardEnable(false)
//                .navigationBarColor(R.color.colorPrimary)
//                .init()
//    }


}
