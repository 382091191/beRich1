package com.ninja.browser.free.video.downloader.base_screen

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.widget.TextView
import com.berich.minlib.R
import com.berich.minlib.base_screen.BasePop

class BaseTipPop(context: Context) : BasePop(context), View.OnClickListener {
    override fun getLayoutId(): Int = R.layout.pop_base

    var titleTv: TextView? = null
    var desTv: TextView? = null
    var listener: OnSubmitClickListener? = null
    override fun initView() {
        setGravity(Gravity.CENTER)
        titleTv = contentView.findViewById(R.id.tv_title)
        desTv = contentView.findViewById(R.id.tv_des)
        contentView.findViewById<View>(R.id.btn_cancle).setOnClickListener(this)
        contentView.findViewById<View>(R.id.btn_submit).setOnClickListener(this)
    }

    fun setTitle(title: String) {
        titleTv?.text = title
    }

    fun setDes(des: String) {
        desTv?.text = des
    }

    fun setOnSubmitClickListener(l: OnSubmitClickListener) {
        listener = l
    }

    interface OnSubmitClickListener {
        fun onSubmit()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_cancle -> {

            }
            R.id.btn_submit -> {
                listener?.onSubmit()
            }
        }
        dismiss()
    }
    override fun onCreateShowAnimation(): Animation? {
        return getDefaultAlphaAnimation(true)
    }

    override fun onCreateDismissAnimation(): Animation? {
        return getDefaultAlphaAnimation(false)
    }

}