package com.berich.minlib.utils

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.berich.minlib.App
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.lang.ref.WeakReference

object SoftKeyBoardUtil {

    val subKeyboardStatus: Subject<Boolean> = PublishSubject.create<Boolean>().toSerialized()
    private const val KEYBOARD_MIN_HEIGHT_RATIO = 0.15

    /**
     * 显示键盘
     */
    fun showSoftKeyboard(editText: EditText, delayMillis: Int = 100) {
        App.getMainHandler().postDelayed({
            editText.requestFocus()
            val imm = editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
        }, delayMillis.toLong())
    }

    /**
     * 隐藏键盘
     */
    fun hideSoftKeyboard(activity: Activity) {
        if (activity.window.attributes.softInputMode
                != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (activity.currentFocus != null) {
                val inputMethodManager = activity
                        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(
                    activity.currentFocus!!.windowToken,
                        InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
    }

    private fun getActivityRoot(activity: Activity): View {
        return (activity.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
    }

    fun registerEventListener(activity: Activity, listener: KeyboardVisibilityEventListener? = null): Unregister {
        val softInputAdjust = (activity.window.attributes.softInputMode and WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST)
        require(softInputAdjust and WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING != WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING) { "Parameter:activity window SoftInputMethod is SOFT_INPUT_ADJUST_NOTHING. In this case window will not be resized" }

        val activityRoot = getActivityRoot(activity)
        val layoutListener: OnGlobalLayoutListener = object : OnGlobalLayoutListener {
            private val r = Rect()
            private var wasOpened = false
            override fun onGlobalLayout() {
                activityRoot.getWindowVisibleDisplayFrame(r)
                val screenHeight = activityRoot.rootView.height
                val heightDiff = screenHeight - r.height()
                val isOpen = heightDiff > screenHeight * KEYBOARD_MIN_HEIGHT_RATIO
                if (isOpen == wasOpened) { // keyboard state has not changed
                    return
                }
                wasOpened = isOpen
                subKeyboardStatus.onNext(isOpen)
                listener?.onVisibilityChanged(isOpen)
            }
        }
        activityRoot.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)
        return SimpleUnregister(activity, layoutListener)
    }

    class SimpleUnregister(activity: Activity, globalLayoutListener: OnGlobalLayoutListener) : Unregister {
        private val mActivityWeakReference: WeakReference<Activity> = WeakReference(activity)
        private val mOnGlobalLayoutListenerWeakReference: WeakReference<OnGlobalLayoutListener> = WeakReference(globalLayoutListener)

        override fun unregister() {
            val activity = mActivityWeakReference.get()
            val globalLayoutListener = mOnGlobalLayoutListenerWeakReference.get()
            if (null != activity && null != globalLayoutListener) {
                val activityRoot = getActivityRoot(activity)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    activityRoot.viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)
                } else {
                    activityRoot.viewTreeObserver.removeGlobalOnLayoutListener(globalLayoutListener)
                }
            }
            mActivityWeakReference.clear()
            mOnGlobalLayoutListenerWeakReference.clear()
        }
    }


    interface KeyboardVisibilityEventListener {
        fun onVisibilityChanged(isOpen: Boolean)
    }

    interface Unregister {
        fun unregister()
    }
}