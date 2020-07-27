/*
 * // Copyright 2019 Mars Development
 * //
 * // Licensed under the Apache License, Version 2.0 (the "License");
 * // you may not use this file except in compliance with the License.
 * // You may obtain a copy of the License at
 * //
*/
package com.berich.minlib.base_screen

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.berich.minlib.R
import com.gyf.immersionbar.ktx.immersionBar
import com.ninja.browser.free.video.downloader.base_screen.MvpRepository
import com.ninja.browser.free.video.downloader.base_screen.MvpView
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle
import com.trello.rxlifecycle3.LifecycleProvider
import com.berich.minlib.utils.LocaleUtil

abstract class BaseActivity<T : BasePresenter<out MvpView, out MvpRepository>>
    : AppCompatActivity(), MvpView {

    companion object {
        const val TAG = "BaseActivity"
    }

    protected var presenter: T? = null
        private set

    private val provider: LifecycleProvider<Lifecycle.Event> = AndroidLifecycle.createLifecycleProvider(getLifecycleOwner())

    public var baseTitle: View? = null
    protected var titleBuilder: ToolBarBuilder? = null

    protected abstract fun onControllerGetContentLayoutId(): Int
    override fun getTitleText(): String? = ""

    override fun getLifecycleOwner(): LifecycleOwner = this
    override fun getLifecycleProvider(): LifecycleProvider<Lifecycle.Event> = provider

    override fun initToolbar(title: String?) {
    }

    override fun addListeners() {
    }

    override fun clearListeners() {
    }

    override fun onHideKeyboard() {
        Log.e("keyboard ","hide")
    }

    override fun onShowKeyboard() {
        Log.e("keyboard ","show")
    }

//    override fun attachBaseContext(newBase: Context?) {
//        super.attachBaseContext(LocaleUtil.ContextWrapper.wrap(newBase))
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        presenter = initPresenter() as? T
        setContentView(onControllerGetContentLayoutId())
        immersionBar {
            statusBarDarkFont(true, 0.2f)
            statusBarColor(R.color.primary)
            fitsSystemWindows(true)
        }
        baseTitle = findViewById(R.id.basetitle)
        if (null != baseTitle) {
            titleBuilder = ToolBarBuilder(this)
            titleBuilder?.showBack(true)
        }
        presenter?.onViewCreated()
        initView()
    }

    override fun onStart() {
        super.onStart()
        presenter?.onStart()
    }

    override fun onResume() {
        super.onResume()
        presenter?.onResume()
    }

    override fun onPause() {
        presenter?.onPause()
        super.onPause()
    }

    override fun onStop() {
        presenter?.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        presenter?.onDestroy()
        presenter = null
        super.onDestroy()
    }

    override fun getContext(): Context? {
        return this
    }

    abstract fun initView()
    override fun toast(tip: String) {
        Toast.makeText(this, tip, Toast.LENGTH_SHORT).show()
    }

    inner class ToolBarBuilder {
        var backBtn: ImageView? = null
        var titleTv: TextView? = null
        var right1Iv: ImageView? = null
        var right2Iv: ImageView? = null
        var rightTv: TextView? = null
        private var title: String? = null
        private var right1Res = -1
        private var right2Res = -1
        private var right1Listener: View.OnClickListener? = null
        private var right2Listener: View.OnClickListener? = null
        private var rightTextListener: View.OnClickListener? = null
        private var rightText: String? = null
        private var isBackShow = false
        private var titleTextColor = 0
        private var rightTextColor = 0
        private var backRes = -1
        private var mContext: Context? = null

        constructor(context: Context) {
            mContext = context
            if (null == baseTitle) {
                throw RuntimeException("没有 include base title")
            } else {
                titleTv = baseTitle!!.findViewById(R.id.title_tv)
                right1Iv = baseTitle!!.findViewById(R.id.right1_img)
                right2Iv = baseTitle!!.findViewById(R.id.right2_img)
                rightTv = baseTitle!!.findViewById(R.id.tv_right)
                backBtn = baseTitle!!.findViewById(R.id.btn_back)
            }

        }

        fun showBack(isShow: Boolean): ToolBarBuilder {
            isBackShow = isShow
            return this
        }

        fun setTitle(title: String?): ToolBarBuilder {
            this.title = title
            return this
        }

        fun setBackImg(res: Int): ToolBarBuilder {
            backBtn!!.setImageResource(res)
            return this
        }

        fun setTitleColor(color: Int): ToolBarBuilder {
            titleTextColor = color
            return this
        }

        fun setRightTextColor(color: Int): ToolBarBuilder {
            rightTextColor = color
            return this
        }

        fun setRight1Img(res: Int, l: View.OnClickListener?): ToolBarBuilder {
            right1Res = res
            right1Listener = l
            return this
        }

        fun showRightImg(isShow: Boolean) {
            if (isShow)
                right1Iv?.visibility = View.VISIBLE
            else
                right1Iv?.visibility = View.GONE
        }

        fun setRight2Img(res: Int, l: View.OnClickListener?): ToolBarBuilder {
            right2Res = res
            right2Listener = l
            return this
        }

        fun setRightText(res: String?, l: View.OnClickListener?): ToolBarBuilder {
            rightText = res
            rightTextListener = l
            return this
        }
        fun showRightText(isShow: Boolean){
            if (isShow)
                rightTv?.visibility = View.VISIBLE
            else
                rightTv?.visibility = View.GONE
        }

        fun setBackIcon(res: Int): ToolBarBuilder {
            backRes = res
            return this
        }

        fun build() {
            if (!TextUtils.isEmpty(title)) {
                titleTv?.text = title
            }
            if (titleTextColor > 0) {
                titleTv?.setTextColor(getResources().getColor(titleTextColor))
            }
            if (right1Res > 0) {
                right1Iv?.setImageResource(right1Res)
                right1Iv?.setOnClickListener(right1Listener)
            }
            if (right2Res > 0) {
                right2Iv?.setImageResource(right2Res)
                right2Iv?.setOnClickListener(right2Listener)
            }
            if (rightTextColor > 0) {
                rightTv?.setTextColor(getResources().getColor(rightTextColor))
            }
            if (!TextUtils.isEmpty(rightText)) {
                rightTv?.text = rightText
                rightTv?.setOnClickListener(rightTextListener)
            }
            if (isBackShow) {
                backBtn?.visibility = View.VISIBLE
                backBtn?.setOnClickListener { v: View? -> onBackPressed() }
            }
            if (backRes != -1) {
                backBtn?.setImageResource(backRes)
            }
        }

        fun setBackgroundColor() {}


    }
}
