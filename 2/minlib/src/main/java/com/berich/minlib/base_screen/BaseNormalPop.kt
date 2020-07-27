package com.ninja.browser.free.video.downloader.base_screen

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow

/**
 * 系统自带popupwindow简单封装
 */
abstract class BaseNormalPop : PopupWindow {
    var mContext: Context? = null

    constructor(context: Context) :super(context){
        mContext = context
        contentView = View.inflate(context, getLayoutId(), null)
        setBackgroundDrawable(ColorDrawable())
        isOutsideTouchable = mIsOutsideTouchable()
        isFocusable = true
        initView()
    }

    abstract fun initView()

    override fun showAsDropDown(anchor: View?) {
        changeBackground(true)
        super.showAsDropDown(anchor)
    }

    override fun showAsDropDown(anchor: View?, xoff: Int, yoff: Int) {
        changeBackground(true)
        super.showAsDropDown(anchor, xoff, yoff)
    }

    override fun showAsDropDown(anchor: View?, xoff: Int, yoff: Int, gravity: Int) {
        changeBackground(true)
        super.showAsDropDown(anchor, xoff, yoff, gravity)
    }

    override fun showAtLocation(parent: View?, gravity: Int, x: Int, y: Int) {
        changeBackground(true)
        super.showAtLocation(parent, gravity, x, y)
    }

    override fun dismiss() {
        changeBackground(false)
        super.dismiss()
    }


    private fun changeBackground(isShow: Boolean) {
        var activity = mContext as Activity
        val lp: WindowManager.LayoutParams = activity.window.attributes
        if (isShow) {
            lp.alpha = 0.5f
        } else {
            lp.alpha = 1f
        }
        activity.window.attributes = lp
    }

    protected fun mIsOutsideTouchable(): Boolean = true
    abstract fun getLayoutId(): Int

}