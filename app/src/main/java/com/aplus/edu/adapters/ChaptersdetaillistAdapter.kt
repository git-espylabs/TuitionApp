package com.aplus.edu.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aplus.edu.R
import com.aplus.edu.api.response.ChapterdetsItem
import com.aplus.edu.constants.AppConstants
import com.bumptech.glide.Glide


class ChaptersdetaillistAdapter internal constructor(
    private val context: Context,
    private val mData: List<ChapterdetsItem>,
    val listner: Chapterdetcommunicator
) : RecyclerView.Adapter<ChaptersdetaillistAdapter.ViewHolder>() {

    inner class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        internal var tutor_image: ImageView = itemView.findViewById(R.id.tutor_image)
        internal var chap_title_tv: TextView = itemView.findViewById(R.id.chap_title_tv)
        internal var main_head: TextView = itemView.findViewById(R.id.main_head)
        internal var main_head_desc_tv: TextView = itemView.findViewById(R.id.main_head_desc_tv)

        internal var tv_duration: TextView = itemView.findViewById(R.id.tv_duration)
    }

    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.chapter_detailsq_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mData[position]
        holder.chap_title_tv.text = data.chapter
        holder.main_head.text = data.videoTitle
        holder.main_head_desc_tv.text = data.shortDesc
        holder.tv_duration.text=data.videoDuration

        Glide.with(context)
            .load(AppConstants.VIDEO_URL + data.videoThumb)
            //.apply(RequestOptions.circleCropTransform())
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.tutor_image)

        holder.itemView.setOnClickListener {
            listner.onRowClick(data)
        }
    }
    interface Chapterdetcommunicator {
        fun onRowClick(data: ChapterdetsItem)
    }
}