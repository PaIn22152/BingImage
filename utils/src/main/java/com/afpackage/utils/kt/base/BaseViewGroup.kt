package com.afpackage.utils.kt.base

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.afpackage.utils.kt.logD

/**
 * Project    AppLock
 * Path       com.afpackage.utils.kt.base
 * Date       2020/03/18 - 10:58
 * Author     Payne.
 * About      类描述：
 */
abstract class BaseViewGroup : ViewGroup {

    constructor(context: Context) : super(context) {
        this.d(" BaseViewGroup 1")
        this.initView(context)

    }


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.d(" BaseViewGroup 2")
        this.initView(context, attrs)
    }


    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        this.d(" BaseViewGroup 3")
        this.initView(context, attrs, defStyleAttr)
    }

    protected open fun initView(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    ) {
        d(" BaseViewGroup initView ")
    }


    protected open fun d(s: String) {
        logD("viewGroupTag", s)
    }
}