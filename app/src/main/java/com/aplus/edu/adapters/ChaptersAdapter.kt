package com.aplus.edu.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aplus.edu.R
import com.aplus.edu.api.response.ChaptersItem


class ChaptersAdapter internal constructor(
    private val context: Context,
    private val mData: List<ChaptersItem>,
    val listner: Chaptercommunicator
) : RecyclerView.Adapter<ChaptersAdapter.ViewHolder>() {

    inner class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        internal var chapter: TextView = itemView.findViewById(R.id.chapter)
        internal var video_count: TextView = itemView.findViewById(R.id.video_count)
        internal var chapter_desc: TextView = itemView.findViewById(R.id.chapter_desc)
    }

    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.row_item_chapters, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mData[position]
        holder.chapter.text = data.chapterTitle
        holder.chapter_desc.text = data.lessonName
        holder.video_count.text = data.videocount.toString() + "Videos"
        holder.itemView.setOnClickListener {
            listner.onRowClick(data)
        }
    }
    interface Chaptercommunicator {
        fun onRowClick(data: ChaptersItem)
    }
}