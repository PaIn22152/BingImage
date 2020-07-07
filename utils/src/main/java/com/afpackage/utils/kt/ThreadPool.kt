package com.afpackage.utils.kt

import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


/**
 * Project    AppLock
 * Path       com.afpackage.utils.kt
 * Date       2020/03/24 - 14:33
 * Author     Payne.
 * About      类描述：
 */
class ThreadPool {
    companion object {
        var cachedThreadPool: ExecutorService = Executors.newCachedThreadPool()

        fun runThread(run: Runnable) {
            cachedThreadPool.execute(run)
        }

        fun <T> runThread(task: Callable<T>) {
            cachedThreadPool.submit(task)
        }

    }
}