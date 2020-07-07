package com.afpackage.utils.kt

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


/**
 * Project    AppLock
 * Path       com.afpackage.utils.kt
 * Date       2020/03/23 - 15:18
 * Author     Payne.
 * About      类描述：
 */

fun createFile(path: String): Boolean {
    try {
        val file = File(path)
        if (file.exists()) {
            return true
        }
        val parentFile = file.parentFile
        if (!parentFile.exists()) {
            parentFile.mkdirs()
        }
        file.createNewFile()
    } catch (e: Exception) {
        e.printStackTrace()
        logD(" createFile false e = $e")
    } finally {
        return File(path).exists()
    }
}


fun copyFile(srcFile: String, destFile: String): Boolean {
    return try {
        val create = createFile(destFile)
        if (!create) {
            return false
        }
        val streamFrom = FileInputStream(srcFile)
        val streamTo = FileOutputStream(destFile)
        val buffer = ByteArray(1024)
        var len: Int = streamFrom.read(buffer)
        while (len > 0) {
            streamTo.write(buffer, 0, len)
            len = streamFrom.read(buffer)
        }
        streamFrom.close()
        streamTo.close()
        true
    } catch (ex: Exception) {
        logD(" copyFile false e = $ex")
        false
    }

}

fun delFile(path: String): Boolean {
    return try {
        val file = File(path)
        file.delete()
    } catch (e: Exception) {
        logD(" delFile false e = $e")
        false
    }

}

fun moveFile(srcFile: String, destFile: String): Boolean {
    val c = copyFile(srcFile, destFile)
    if (c) {
        return delFile(srcFile)
    }
    return false
}
