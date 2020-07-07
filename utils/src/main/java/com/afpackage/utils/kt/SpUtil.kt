package com.afpackage.utils.kt

import android.content.Context
import android.content.SharedPreferences
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method


/**
 * Project    AppLock
 * Path       com.aftest.applock.utils
 * Date       2020/03/16 - 17:33
 * Author     Payne.
 * About      类描述：
 */
open class SpUtil(context: Context, name: String) {
    private val mSp: SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)
    private val mEditor: SharedPreferences.Editor = mSp.edit()

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     */
     fun put(key: String, `object`: Any) {
        when (`object`) {
            is String -> mEditor.putString(key, `object`)
            is Int -> mEditor.putInt(key, `object`)
            is Boolean -> mEditor.putBoolean(key, `object`)
            is Float -> mEditor.putFloat(key, `object`)
            is Long -> mEditor.putLong(key, `object`)
            else -> mEditor.putString(key, `object`.toString())
        }
        SharedPreferencesCompat.apply(mEditor)
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     */
     operator fun get(key: String, defaultObject: Any): Any? {
        return when (defaultObject) {
            is String -> mSp.getString(key, defaultObject)
            is Int -> mSp.getInt(key, defaultObject)
            is Boolean -> mSp.getBoolean(key, defaultObject)
            is Float -> mSp.getFloat(key, defaultObject)
            is Long -> mSp.getLong(key, defaultObject)
            else -> null
        }

    }

    /**
     * 移除某个key值已经对应的值
     */
    fun remove(key: String) {
        mEditor.remove(key)
        SharedPreferencesCompat.apply(mEditor)
    }

    /**
     * 清除所有数据
     */
    fun clear() {
        mEditor.clear()
        SharedPreferencesCompat.apply(mEditor)
    }


    /**
     * 查询某个key是否已经存在
     */
    operator fun contains(key: String): Boolean {
        return mSp.contains(key)
    }


    /**
     * 返回所有的键值对
     */
    fun getAll(): Map<String, *> {
        return mSp.all
    }


    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     */
    private object SharedPreferencesCompat {

        private val sApplyMethod =
            findApplyMethod()

        /**
         * 反射查找apply的方法
         */
        private fun findApplyMethod(): Method? {
            try {
                val clz = SharedPreferences.Editor::class.java
                return clz.getMethod("apply")
            } catch (e: NoSuchMethodException) {
            }

            return null
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         */
        fun apply(editor: SharedPreferences.Editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor)
                    return
                }
            } catch (e: IllegalArgumentException) {
            } catch (e: IllegalAccessException) {
            } catch (e: InvocationTargetException) {
            }

            editor.commit()
        }
    }


}