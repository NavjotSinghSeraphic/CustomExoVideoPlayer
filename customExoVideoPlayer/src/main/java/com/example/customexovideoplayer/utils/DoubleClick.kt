package com.example.customexovideoplayer.utils

import android.os.Handler
import android.os.Looper
import android.view.View

/*
open class DoubleClick(private val doubleClickListener: DoubleClickListener, private var interval: Long = 200L) : View.OnClickListener {

    private val handler = Handler(Looper.getMainLooper())
    private var counterClicks = 0
    private var isBusy = false

    override fun onClick(view: View) {
        if (!isBusy) {
            isBusy = true

            counterClicks++
            handler.postDelayed({
                if (counterClicks >= 2) {
                    doubleClickListener.onDoubleClickEvent(view)
                }
                if (counterClicks == 1) {
                    doubleClickListener.onSingleClickEvent(view)
                }

                counterClicks = 0
            }, interval)
            isBusy = false
        }
    }

}*/
class DoubleClick(private val listener: DoubleClickListener) : View.OnClickListener {
    private var lastClickTime: Long = 0
    private val DOUBLE_CLICK_TIME_DELTA: Long = 300 // Milliseconds

    override fun onClick(view: View) {
        val clickTime = System.currentTimeMillis()
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
            listener.onDoubleClickEvent(view)
        } else {
            listener.onSingleClickEvent(view)
        }
        lastClickTime = clickTime
    }
}
