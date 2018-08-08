package com.automayta.gmspinner

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.util.*

/**
 * Created By Uzair Mohammad Rather On 09/25/2017
 */
class CoreSpinnerPopDownWindowRVAdapter(private val mValues: HashMap<Any, Any>, private val cxt: Context, coreSpinnerView: CoreSpinnerView) : RecyclerView.Adapter<CoreSpinnerPopDownWindowRVAdapter.CoreSpinnerViewHolder>() {
    internal var coreSpinnerView: CoreSpinnerView? = null
    internal var itemSelectedListener: ItemSelected = coreSpinnerView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoreSpinnerViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.core_spinner_dropdown_item, parent, false)
        return CoreSpinnerViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoreSpinnerViewHolder, position: Int) {

        var i = 0
        for (mItem in mValues.entries) {
            if (position == i) {
                holder.key = (mItem as MutableMap.MutableEntry<*, *>).key
                holder.value = (mItem as MutableMap.MutableEntry<*, *>).value
                break
            }
            i++
        }

        holder.spinnerItem.text = holder.value!!.toString()
        holder.mView.setOnClickListener { itemSelectedListener.OnDropDownItemSelectedListener(holder.key, holder.value!!.toString()) }

    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class CoreSpinnerViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val spinnerItem: TextView
        var key: Any? = null
        var value: Any? = null

        init {
            spinnerItem = mView.findViewById<View>(R.id.spinnerItem) as TextView

        }
    }


    interface ItemSelected {
        fun OnDropDownItemSelectedListener(key: Any?, value: String)
    }
}
