package com.berich.minlib.base_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable

abstract class BaseMvcFragment : Fragment() {

    var mRootView: View? = null
    var compositeDisposable = CompositeDisposable()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (null == mRootView) {
            mRootView = View.inflate(activity, getLayoutRes(), null)
        }
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view,savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
    fun toastTip(tip: String) {
        Toast.makeText(activity, tip, Toast.LENGTH_SHORT).show()
    }

    abstract fun initView(view:View,savedInstanceState:Bundle?)
    abstract fun getLayoutRes(): Int
}