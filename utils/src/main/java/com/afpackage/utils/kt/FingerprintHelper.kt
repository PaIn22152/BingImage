package com.afpackage.utils.kt

import android.annotation.TargetApi
import android.content.Context
import android.content.DialogInterface
import android.hardware.biometrics.BiometricPrompt
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.CancellationSignal


/**
 * Project    AppLock
 * Path       com.afpackage.utils.kt
 * Date       2020/03/18 - 16:53
 * Author     Payne.
 * About      类描述：
 *
 * 指纹识别
 *
need permissions
<uses-permission android:name="android.permission.USE_BIOMETRIC" />
<uses-permission android:name="android.permission.USE_FINGERPRINT"/>
 */
class FingerprintHelper(context: Context) {

    interface OnFingerprintListener {
        fun onUnsupported()//不支持指纹识别
        fun onNotEnrolled()//支持指纹识别，但没有录入指纹
        fun onFailed()//识别失败
        fun onSucceeded()//识别成功
        fun onAuthenticationError(errorCode: Int, errString: CharSequence?)
    }


    companion object {
        var fingerprintHelper: FingerprintHelper? = null

        fun getInstance(context: Context): FingerprintHelper {
            if (fingerprintHelper == null) {
                synchronized(FingerprintHelper) {
                    fingerprintHelper = FingerprintHelper(context.applicationContext)
                }
            }
            return fingerprintHelper!!
        }
    }


    private var mContext: Context? = context
    private var fingerprintManager: FingerprintManager? = null
    private var mCancellationSignal: CancellationSignal? = null
    private var biometricPrompt: BiometricPrompt? = null

    init {
        if (SDK_INT >= Build.VERSION_CODES.M) {
            mCancellationSignal = CancellationSignal()
            fingerprintManager = mContext!!.getSystemService(FingerprintManager::class.java)
        }


        if (SDK_INT >= Build.VERSION_CODES.P) {
            biometricPrompt = BiometricPrompt
                .Builder(mContext)
                //Dialog的标题
                .setTitle("指纹验证")
                //Dialog的副标题
                .setSubtitle("这里是subTitle")
                //Dialog的提示内容
                .setDescription("描述")
                .setNegativeButton("Cancel", context.mainExecutor,
                    DialogInterface.OnClickListener { dialog, _ ->
                        logD(" Cancel ")
                        dialog?.dismiss()
                    })
                .build()
        }
    }


    private fun log(s: String) {
        logD("fmTag", s)
    }


    //todo api28指纹识别没有测试
    @TargetApi(Build.VERSION_CODES.P)
    private fun listening28(listener: OnFingerprintListener) {


        biometricPrompt?.authenticate(
            mCancellationSignal!!,
            mContext!!.mainExecutor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {

                }

                override fun onAuthenticationFailed() {
                    listener.onFailed()
                }

                override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {

                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    listener.onSucceeded()
                }
            }
        )
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun listening23(listener: OnFingerprintListener) {
        fingerprintManager!!.authenticate(
            null,
            mCancellationSignal,
            0,
            object : FingerprintManager.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {

                    log(" onAuthenticationError ")

                    if (errorCode == FingerprintManager.FINGERPRINT_ERROR_LOCKOUT) {

                    }

                }

                override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence) {
                    log("onAuthenticationHelp")
                }

                override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult) {
                    listener.onSucceeded()
                    log("onAuthenticationSucceeded")
                }

                override fun onAuthenticationFailed() {
                    listener.onFailed()
                    log("onAuthenticationFailed")
                }
            },
            null
        )
    }


    fun startListening(listener: OnFingerprintListener) {

        if (!canUseFingerprint()) {
            listener.onUnsupported()
            return
        } else if (!hasEnrolledFingerprints()) {
            listener.onNotEnrolled()
            return
        } else if (SDK_INT >= Build.VERSION_CODES.M
            && SDK_INT < Build.VERSION_CODES.P
        ) {//api [23,28)
            listening23(listener)
            return
        } else {//api 大于大于28
            listening23(listener)
//            listening28(listener)
            return
        }

    }


    /**
     * 手机是否支持指纹识别功能
     * */
    fun canUseFingerprint(): Boolean {
        try {
            return if (SDK_INT < Build.VERSION_CODES.M) {//手机api版本低于23的，不支持指纹识别
                false
            } else if (SDK_INT >= Build.VERSION_CODES.M
                && SDK_INT < Build.VERSION_CODES.P
            ) {//api [23,28)
                fingerprintManager!!.isHardwareDetected
            } else {//api 大于大于28
                //todo api28判断手机是否支持指纹
                fingerprintManager!!.isHardwareDetected
            }
        } catch (e: Exception) {

        }
        return false
    }

    /**
     * 是否录入指纹
     * */
    fun hasEnrolledFingerprints(): Boolean {
        try {
            return if (SDK_INT < Build.VERSION_CODES.M) {//手机api版本低于23的，不支持指纹识别
                false
            } else if (SDK_INT >= Build.VERSION_CODES.M
                && SDK_INT < Build.VERSION_CODES.P
            ) {//api [23,28)
                fingerprintManager!!.hasEnrolledFingerprints()
            } else {//api 大于大于28
                //todo api28判断是否录入指纹
                fingerprintManager!!.hasEnrolledFingerprints()
            }
        } catch (e: Exception) {

        }
        return false
    }


    fun stopListening() {
//        if (SDK_INT >= Build.VERSION_CODES.M) {
//            if (mCancellationSignal != null) {
//                mCancellationSignal!!.cancel()
//            }
//        }
        mCancellationSignal = null
        fingerprintHelper = null

    }


}