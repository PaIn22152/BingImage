package com.afpackage.utils.kt.base

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.afpackage.utils.kt.dp2Px
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.CropSquareTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 * Project    AppLock
 * Path       com.afpackage.utils.kt.base
 * Date       2020/03/13 - 16:26
 * Author     Payne.
 * About      类描述：
 */

open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun <T : View> findViewById(id: Int): T? {
        return itemView.findViewById(id)
    }

    fun setTextViewText(id: Int, text: String) {
        val textView = findViewById<TextView>(id)
        //通过getViewById获得view后，判空，防止空指针导致崩溃
        textView?.text = text

    }

    fun setTextViewColor(id: Int, color: Int) {
        val textView = findViewById<TextView>(id)
        //通过getViewById获得view后，判空，防止空指针导致崩溃
        textView?.setTextColor(color)
    }

    fun setImageViewDrawable(id: Int, icon: Drawable) {
        val imageView = findViewById<ImageView>(id)
        //通过getViewById获得view后，判空，防止空指针导致崩溃
        imageView?.setImageDrawable(icon)

    }

    fun setImageViewResource(id: Int, resId: Int) {
        val imageView = findViewById<ImageView>(id)
        //通过getViewById获得view后，判空，防止空指针导致崩溃
        imageView?.setImageResource(resId)
    }

    fun setImageViewPath(id: Int, path: String) {
        val imageView = findViewById<ImageView>(id)
        //通过getViewById获得view后，判空，防止空指针导致崩溃
        if (imageView != null) {
            Glide.with(itemView.context).load(path)
                .into(imageView)
        }
    }

    //圆角
    fun setImageViewPathCorners(id: Int, path: String) {
        val imageView = findViewById<ImageView>(id)
        //通过getViewById获得view后，判空，防止空指针导致崩溃
        if (imageView != null) {

            val multi = MultiTransformation<Bitmap>(
                CropSquareTransformation(),
                RoundedCornersTransformation(dp2Px(10), 0)
            )


            Glide.with(itemView.context).load(path)
                .apply(
                    RequestOptions.bitmapTransform(
//                        CropSquareTransformation()
                        RoundedCornersTransformation(dp2Px(5), 0)
//                        multi
                    )
                )
                .into(imageView)
        }
    }

    fun setViewClickListener(id: Int, listener: View.OnClickListener) {
        val view = findViewById<View>(id)
        view?.setOnClickListener { v -> listener.onClick(v) }
    }

    fun setViewVisibility(id: Int, visibility: Boolean) {
        val view = findViewById<View>(id)
        view?.visibility = if (visibility) View.VISIBLE else View.GONE
    }


    fun setSwitch(id: Int, on: Boolean) {
        val swi = findViewById<Switch>(id)
        //通过getViewById获得view后，判空，防止空指针导致崩溃
        if (swi != null) {
            swi.isChecked = on
        }
    }
    // todo 可以扩展其他类似的方法

}