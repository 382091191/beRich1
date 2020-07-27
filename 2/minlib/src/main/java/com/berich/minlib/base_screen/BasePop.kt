package com.berich.minlib.base_screen

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import razerdp.basepopup.BasePopupWindow

/**
 * 三方pupwindow二次简单封装
 */
abstract class BasePop : BasePopupWindow {
    private var mContext:Context?=null
    constructor(context: Context) : super(context) {
        mContext = context
        popupGravity = Gravity.BOTTOM//默认从底部
        initView()
    }

    override fun onCreateContentView(): View = createPopupById(getLayoutId())

    abstract fun getLayoutId(): Int
    abstract fun initView();

    fun setGravity(gravity: Int) {
        popupGravity = gravity
    }


    override fun onCreateShowAnimation(): Animation? {
        return getTranslateVerticalAnimation(1f, 0f, 300)
    }

    override fun onCreateDismissAnimation(): Animation? {
        return getTranslateVerticalAnimation(0f, 1f, 300)
    }




//    override fun onCreateShowAnimation(): Animation? {
//        return getDefaultAlphaAnimation(true)
//    }
//
//    override fun onCreateDismissAnimation(): Animation? {
//        return getDefaultAlphaAnimation(false)
//    }

}