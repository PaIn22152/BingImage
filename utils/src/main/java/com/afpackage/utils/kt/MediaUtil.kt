package com.afpackage.utils.kt

import android.content.Context
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import java.io.File
import android.provider.MediaStore.Video
import android.provider.MediaStore.Images
import android.content.ContentResolver
import android.database.Cursor


/**
 * Project    AppLock
 * Path       com.afpackage.utils.kt
 * Date       2020/03/19 - 16:57
 * Author     Payne.
 * About      类描述：
 */

class MediaUtil {
    companion object {

        private fun getPhotos(context: Context): ArrayList<String> {
            var cursor: Cursor? = null
            try {
                val res = ArrayList<String>()
                val imageUri = Images.Media.EXTERNAL_CONTENT_URI
                val contentResolver = context.contentResolver
                val projection = arrayOf(
                    Images.ImageColumns.DATA,
                    Images.ImageColumns.DISPLAY_NAME,
                    Images.ImageColumns.SIZE,
                    Images.ImageColumns.DATE_ADDED
                )
                cursor = contentResolver.query(
                    imageUri, projection, null, null,
                    Images.Media.DATE_ADDED + " desc"
                )
                    ?: return res
                logD("")
                if (cursor.count != 0) {
                    while (cursor.moveToNext()) {
                        val path =
                            cursor.getString(cursor.getColumnIndex(Images.ImageColumns.DATA))
//                        val fileName =
//                            cursor.getString(cursor.getColumnIndex(Images.Media.DISPLAY_NAME))
//                        val size = cursor.getString(cursor.getColumnIndex(Images.Media.SIZE))
//                        val date =
//                            cursor.getString(cursor.getColumnIndex(Images.Media.DATE_ADDED))

                        res.add(path)

//                    logD("  getPhotos  path = $path   name=$fileName")

                        try {
//                            val exifInterface = ExifInterface(path)
//                            val TAG_APERTURE =
//                                exifInterface.getAttribute(ExifInterface.TAG_APERTURE)
//                            val TAG_DATETIME =
//                                exifInterface.getAttribute(ExifInterface.TAG_DATETIME)
//                            val TAG_EXPOSURE_TIME =
//                                exifInterface.getAttribute(ExifInterface.TAG_EXPOSURE_TIME)
//                            val TAG_FLASH = exifInterface.getAttribute(ExifInterface.TAG_FLASH)
//                            val TAG_FOCAL_LENGTH =
//                                exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH)
//                            val TAG_IMAGE_LENGTH =
//                                exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH)
//                            val TAG_IMAGE_WIDTH =
//                                exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH)
//                            val TAG_ISO = exifInterface.getAttribute(ExifInterface.TAG_ISO)
//                            val TAG_MAKE = exifInterface.getAttribute(ExifInterface.TAG_MAKE)
//                            val TAG_MODEL = exifInterface.getAttribute(ExifInterface.TAG_MODEL)
//                            val TAG_ORIENTATION =
//                                exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION)
//                            val TAG_WHITE_BALANCE =
//                                exifInterface.getAttribute(ExifInterface.TAG_WHITE_BALANCE)

//                        logD("光圈值:$TAG_APERTURE")
//                        logD("拍摄时间:$TAG_DATETIME")
//                        logD("曝光时间:$TAG_EXPOSURE_TIME")
//                        logD("闪光灯:$TAG_FLASH")
//                        logD("焦距:$TAG_FOCAL_LENGTH")
//                        logD("图片高度:$TAG_IMAGE_LENGTH")
//                        logD("图片宽度:$TAG_IMAGE_WIDTH")
//                        logD("ISO:$TAG_ISO")
//                        logD("设备品牌:$TAG_MAKE")
//                        logD("设备型号:$TAG_MODEL")
//                        logD("旋转角度:$TAG_ORIENTATION")
//                        logD("白平衡:$TAG_WHITE_BALANCE")
                        } catch (e: Exception) {

                        }


                    }
                }

                return res
            } finally {
                cursor?.close()

            }
        }

        private fun getVideos(context: Context): ArrayList<String> {
            var cursor: Cursor? = null
            try {
                val res = ArrayList<String>()
                val videoUri = Video.Media.EXTERNAL_CONTENT_URI
                val cr = context.contentResolver
                val projection = arrayOf(
                    Video.VideoColumns.DATA,
                    Video.VideoColumns.DURATION,
                    Video.VideoColumns.DISPLAY_NAME,
                    Video.VideoColumns.DATE_ADDED
                )

                cursor = cr.query(videoUri, projection, null, null, null) ?: return res
                if (cursor.count != 0) {
                    while (cursor.moveToNext()) {
                        val path = cursor.getString(cursor.getColumnIndex(Video.Media.DATA))
//                        val name =
//                            cursor.getString(cursor.getColumnIndex(Video.Media.DISPLAY_NAME))
//                        val date =
//                            cursor.getString(cursor.getColumnIndex(Video.Media.DATE_ADDED))
//                        val duration =
//                            cursor.getLong(cursor.getColumnIndex(Video.Media.DURATION))

                        res.add(path)
                    }
                }

                return res
            } finally {
                cursor?.close()
            }
        }


        private fun group(paths: ArrayList<String>): ArrayList<ArrayList<String>> {
            val sorted = paths.sorted()
            val res = ArrayList<ArrayList<String>>()
            val map = HashMap<String, ArrayList<String>>()
            for (path in sorted) {
                val parentPath = File(path).parent

                if (map.containsKey(parentPath)) {
                    val value = map[parentPath]
                    value?.add(path)
                } else {
                    val value = ArrayList<String>()
                    value.add(path)
                    map[parentPath] = value
                }
            }
            for (list in map) {
                res.add(list.value)
            }
            return res
        }

        fun getPhotosGrouped(context: Context): ArrayList<ArrayList<String>> {
            val medias = getPhotos(context)
            logD("")
            return group(medias)
        }


        fun getVideosGrouped(context: Context): ArrayList<ArrayList<String>> {
            val medias = getVideos(context)
            return group(medias)
        }


    }
}
