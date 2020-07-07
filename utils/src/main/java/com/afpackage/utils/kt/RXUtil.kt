package com.afpackage.utils.kt

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


/**
 * Project    AppLock
 * Path       com.aftest.applock.ktutil
 * Date       2020/03/13 - 15:38
 * Author     Payne.
 * About      类描述：
 */


/**
 *延时任务
 * */
fun rxDelay(delay: Long, mainThread: Boolean, callback: RXCallback): Disposable {
    return Observable
        .timer(delay, TimeUnit.MILLISECONDS)
        .observeOn(
            if (mainThread) {
                AndroidSchedulers.mainThread()
            } else {
                Schedulers.computation()
            }
        ) // 指定 Subscriber 的回调发生在主线程或者子线程
        .doOnNext {
            //            logD(" rxDelay doOnNext it = $it")
            callback.call()
        }
        .subscribe()

}


fun rxMainThreadRun(callback: RXCallback) {
    Observable
        .create<Any> {

        }
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext {
            callback.call()
        }.subscribe()
}


/**
 * 循环任务
 * 使用返回对象的dispose()方法可以取消任务
 * */
fun rxInterval(period: Long, mainThread: Boolean, callback: RXCallback): Disposable {
    return Observable
        .interval(period, TimeUnit.MILLISECONDS)
        .observeOn(
            if (mainThread) {
                AndroidSchedulers.mainThread()
            } else {
                Schedulers.io()
            }
        ) // 指定 Subscriber 的回调发生在主线程或者子线程
        .doOnNext {
            callback.call()
        }
        .subscribe()

}


fun rxMap() {
    val integerList = ArrayList<Int>()
    for (i in 0..2) {
        integerList.add(i)
    }
    Observable
        .fromIterable(integerList)
        .subscribeOn(Schedulers.computation())
//        .observeOn(Schedulers.io())
        .observeOn(Schedulers.computation())
        .map { t ->
            //使用map操作符，实现类型转换，并指定线程
            logD(" map $t")
            "str_$t"//Int 转 String。耗时操作，子线程
        }
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext { s ->
            logD(" doOnNext $s")//主线程处理
        }
        .subscribe()
}


interface RXCallback {
    fun call()
}