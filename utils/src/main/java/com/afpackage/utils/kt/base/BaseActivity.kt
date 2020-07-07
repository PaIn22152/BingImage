package com.afpackage.utils.kt.base

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.afpackage.utils.R


/**
 * Project    AppLock
 * Path       com.afpackage.utils.kt.base
 * Date       2020/03/16 - 11:31
 * Author     Payne.
 * About      类描述：
 */
abstract class BaseActivity : AppCompatActivity() {


    @LayoutRes
    abstract fun setContentId(): Int

    open fun beforeSetContent() {}

    open fun slideLeft2RightFinish(): Boolean {//从屏幕最左边滑动时关闭activity
        return true
    }


    open fun finishAndRemoveTask_() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask()
        } else {
            finish()
        }
    }


    var rootView: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
//        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR

        beforeSetContent()

        setContentView(setContentId())




        if (slideLeft2RightFinish()) {
            rootView = window.decorView.findViewById(android.R.id.content)
        }




        val outMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(outMetrics)
        screenWidth = outMetrics.widthPixels
        screenHeight = outMetrics.heightPixels
    }


    var startX = 0f
    var startY = 0f
    var endX = 0f
    var endY = 0f
    var lastX = 0f
    var screenWidth = 1080
    var screenHeight = 1080

    var startLeftBound = false
    var slide2Right = false

    override fun onTouchEvent(event: MotionEvent): Boolean {

        if (slideLeft2RightFinish()) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = event.x
                    startY = event.y
                    startLeftBound = startX < screenWidth / 100 * 8
//                    logD("  ACTION_DOWN    x=${event.x}  y=${event.y}")
                }
                MotionEvent.ACTION_MOVE -> {

                    endX = event.x
                    endY = event.y

                    slide2Right = endX > lastX
                    lastX = endX

//                    logD("  ACTION_MOVE    x=${event.x}  y=${event.y}")

                    if (startLeftBound) {
                        rootView?.translationX = endX - startX
                    }

                }
                MotionEvent.ACTION_UP -> {
                    endX = event.x
                    endY = event.y
                    if (startLeftBound && slide2Right) {
                        finish()
                        overridePendingTransition(R.anim.in_left, R.anim.out_right)
                    } else if (startLeftBound) {
                        resetAnim()
                    }
//                    logD("  ACTION_UP    x=${event.x}  y=${event.y}")
                }

            }
        }
        return super.onTouchEvent(event)
    }

    private fun resetAnim() {
        if (rootView != null) {
            rootView!!.animate().translationX(0f).duration = 200
        }
    }


}