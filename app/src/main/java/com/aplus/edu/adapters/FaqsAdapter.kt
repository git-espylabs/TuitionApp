package com.aplus.edu.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aplus.edu.R
import com.aplus.edu.api.response.FaqsItem
import com.ms.square.android.expandabletextview.ExpandableTextView
import kotlinx.android.synthetic.main.row_item_faqs.view.*


class FaqsAdapter internal constructor(
    private val context: Context,
    private val mData: List<FaqsItem>,
    val listner: Faqcommunicator
) : RecyclerView.Adapter<FaqsAdapter.ViewHolder>() {

    inner class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {


        internal var faq_tv: TextView = itemView.findViewById(R.id.faq_tv)
        //internal var faq_desc_tv: TextView = itemView.findViewById(R.id.faq_desc_tv1)
        /*internal var expTv2: ExpandableTextView? = itemView.findViewById(R.id.expand_text_view)
            .findViewById(R.id.expandable_text)*/
    }

    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.row_item_faqs, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mData[position]
        holder.faq_tv.text = data.quTitle
        //holder.faq_desc_tv.text = data.quAnswer
        holder.itemView.expand_text_view.text=data.quAnswer
        holder.itemView.setOnClickListener {
            listner.onRowClick(data)
        }
    }

    interface Faqcommunicator {
        fun onRowClick(data: FaqsItem)
    }
}