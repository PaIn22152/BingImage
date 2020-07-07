package com.afpackage.utils.kt

import android.util.Log

/**
 * Project    AppLock
 * Path       com.afpackage.utils.kt
 * Date       2020/03/13 - 15:47
 * Author     Payne.
 * About      类描述：
 */

const val SHOW_LOG = true


fun logD_alive(s: String) {
    logD("alivetag", s)
    //        logD( s);
}

fun logD_window(s: String) {
        logD("windowtag", s);
//    logThread("windowtag", s)
    //        logD(s);
}

fun logD_finger(s: String) {
    logD("fingertag", s)
    //        logThread("windowtag", s);
//        logD(s);
}

fun logD(s: String) {
    logD("defTag", s)
}

fun logD_ad(s: String) {
    logD("adtttag", s)
}

fun logD_NEW_APP(s: String) {
    logD( s)
    logD("newapptag", s)
}

fun logE(s: String) {
    logD("errTag", s)
}

fun logThread(s: String) {
    val thr = Thread.currentThread().name
    logD("thr=$thr  s=$s")

}

var startTime = 0L
fun logTimeStart() {
    startTime = System.currentTimeMillis()
    logD("start ")
    logD("timeTag", "start ")

}

fun logTimeEnd() {
    val time = System.currentTimeMillis() - startTime
    logD("end time=$time")
    logD("timeTag", "end time=$time")


}


fun logD(tag: String, s: String) {
    if (!SHOW_LOG) {
        return
    }
    Log.d(tag, s)
}