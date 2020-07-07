package com.afpackage.utils.kt

import android.app.Activity
import android.os.StatFs
import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.text.format.Formatter


/**
 * Project    AppLock
 * Path       com.afpackage.utils.kt
 * Date       2020/03/24 - 11:26
 * Author     Payne.
 * About      类描述：
 */
class MemoryUtil {
    companion object {


        /**
         * 获取手机总内存
         */
        fun getTotalRAM(context: Context): Long {
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val mi = ActivityManager.MemoryInfo()
            am.getMemoryInfo(mi)
            return mi.totalMem
        }

        fun getTotalRAMStr(context: Context): String {
            return Formatter.formatFileSize(context, getTotalRAM(context))
        }


        /**
         * 获取手机可用内存
         */

        fun getAvailRAM(context: Context): Long {
            val am = context
                .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val mi = ActivityManager.MemoryInfo()
            am.getMemoryInfo(mi)
            return mi.availMem
        }

        fun getAvailRAMStr(context: Context): String {
            return Formatter.formatFileSize(context, getAvailRAM(context))
        }


        /**
         * 获取手机已经使用内存
         */
        fun getRAMUsedPercent(context: Context): Int {
            val totalRAM = getTotalRAM(context)
            val usedRAM = getUsedRAM(context)
            return (usedRAM * 100 / totalRAM).toInt()

        }

        fun getUsedRAM(context: Context): Long {
            return getTotalRAM(context) - getAvailRAM(context)
        }

        fun getUsedRAMStr(context: Context): String {
            return Formatter.formatFileSize(context, getUsedRAM(context))
        }


        private fun phoneSpacePath(): String {
            return Environment.getDataDirectory().absolutePath
        }

        /**
         * 获取手机总存储空间
         */
        fun getSpaceUsedPercent(): Int {
            val totalSpace = getTotalSpace()
            val usedSpace = getUsedSpace()
            return (usedSpace * 100 / totalSpace).toInt()
        }

        fun getTotalSpace(): Long {
            val statfs = StatFs(phoneSpacePath())
            val size = statfs.blockSize.toLong()//获取分区的大小
            val blockCountLong: Long
            blockCountLong =
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    statfs.blockCountLong
                } else {
                    statfs.blockCount.toLong()
                }
            return size * blockCountLong
        }

        fun getTotalSpaceStr(activity: Activity): String {
            return Formatter.formatFileSize(activity, getTotalSpace())
        }


        /**
         * 获取手机可用存储空间
         */
        fun getAvailSpace(): Long {
            val statfs = StatFs(phoneSpacePath())
            val size =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    statfs.blockSizeLong
                } else {
                    statfs.blockSize.toLong()
                }//获取分区的大小
            val count =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    statfs.availableBlocksLong
                } else {
                    statfs.availableBlocks.toLong()
                }//获取可用分区块的个数
            return size * count
        }

        fun getAvailSpaceStr(activity: Activity): String {
            return Formatter.formatFileSize(activity, getAvailSpace())
        }


        /**
         * 获取手机已经使用存储空间
         */
        fun getUsedSpace(): Long {
            return getTotalSpace() - getAvailSpace()
        }

        fun getUsedSpaceStr(context: Context): String {
            return byte2Str(context, getUsedSpace())
        }

        fun byte2Str(context: Context, input: Long): String {
            return Formatter.formatFileSize(context, input)

        }


    }
}