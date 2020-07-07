package com.afpackage.utils.kt

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

/**
 * Project    AppLock
 * Path       com.afpackage.utils.kt
 * Date       2020/04/20 - 14:12
 * Author     Payne.
 * About      类描述：
 */
class UriUtil {

    companion object {

        fun getImageContentUri(context: Context, path: String): Uri? {

            //android 10 加载本地图片，需将path转成uri
            return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                null
            } else try {
                val cursor = context.contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, arrayOf(
                        MediaStore.Images.Media._ID
                    ), MediaStore.Images.Media.DATA + "=? ", arrayOf(path), null
                )
                if (cursor != null && cursor.moveToFirst()) {
                    val id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID))
                    val baseUri =
                        Uri.parse("content://media/external/images/media")
                    Uri.withAppendedPath(baseUri, "" + id)
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }
}