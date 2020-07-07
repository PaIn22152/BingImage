package com.afpackage.utils.kt.base

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.afpackage.utils.kt.logD

/**
 * Project    AppLock
 * Path       com.afpackage.utils.kt.base
 * Date       2020/03/18 - 10:22
 * Author     Payne.
 * About      类描述：
 */
abstract class BaseView : View {




    constructor(context: Context) : super(context) {
        this.d(" BaseView 1")
        this.initView(context)

    }


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.d(" BaseView 2")
        this.initView(context, attrs)
    }


    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        this.d(" BaseView 3")
        this.initView(context, attrs, defStyleAttr)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }

    protected open fun d(s: String) {
        logD("viewTag", s)
    }

    protected open fun initView(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    ) {
        d(" BaseView initView ")
    }
}