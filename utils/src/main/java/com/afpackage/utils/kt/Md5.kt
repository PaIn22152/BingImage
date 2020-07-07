package com.afpackage.utils.kt

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Project    AppLock
 * Path       com.afpackage.utils.kt
 * Date       2020/03/24 - 10:36
 * Author     Payne.
 * About      类描述：
 */
fun MD5Encrypt(str: String?): String {

    require(!(str == null || str.isEmpty())) { "String to encript cannot be null or zero length" }
    val hexString = StringBuffer()
    try {
        val md = MessageDigest.getInstance("MD5")
        md.update(str.toByteArray())
        val hash = md.digest()
        for (i in hash.indices) {
            if (0xff and hash[i].toInt() < 0x10) {
                hexString.append("0" + Integer.toHexString(0xFF and hash[i].toInt()))
            } else {
                hexString.append(Integer.toHexString(0xFF and hash[i].toInt()))
            }
        }
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }

    return hexString.toString()
}