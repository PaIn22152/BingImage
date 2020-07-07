package com.afpackage.utils.kt.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Project    AppLock
 * Path       com.afpackage.utils.kt.base
 * Date       2020/03/13 - 16:25
 * Author     Payne.
 * About      类描述：
 */

abstract class BaseRecyclerViewAdapter1 : RecyclerView.Adapter<BaseViewHolder>() {

    private var mItemClickListener: OnItemClickListener? = null
    private var mItemLongClickListener: OnItemLongClickListener? = null

    override fun onBindViewHolder(viewholer: BaseViewHolder, position: Int) {
        if (mItemClickListener != null) {
            viewholer.itemView.setOnClickListener {
                mItemClickListener?.onItemClick(position, viewholer.itemView)
            }
        }
        if (mItemLongClickListener != null) {
            viewholer.itemView.setOnLongClickListener {
                mItemLongClickListener?.onItemLongClick(position)!!
            }
        }
    }

    fun setItemClickListener(listener: OnItemClickListener) {
        mItemClickListener = listener
    }

    fun setItemLongClickListener(listener: OnItemLongClickListener) {
        mItemLongClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, v: View)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(position: Int): Boolean
    }

}