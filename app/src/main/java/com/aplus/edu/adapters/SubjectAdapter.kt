package com.aplus.edu.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.aplus.edu.R
import com.aplus.edu.api.response.SubjectItem
import com.aplus.edu.constants.AppConstants
import com.makeramen.roundedimageview.RoundedImageView

class SubjectAdapter internal constructor(
    private val context: Context,
    private val mData: List<SubjectItem>,
    val listner: Subjectcommunicator
) : RecyclerView.Adapter<SubjectAdapter.ViewHolder>() {

    inner class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        internal var subImg: RoundedImageView = itemView.findViewById(R.id.subImg)
        internal var subName: TextView = itemView.findViewById(R.id.subName)

    }

    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.subject_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mData[position]
        holder.subName.text = data.subjectName

        Glide.with(context)
            .load(AppConstants.IMAGE_URL + data.subjectImage)
            //.apply(RequestOptions.circleCropTransform())
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.subImg)
        holder.itemView.setOnClickListener {
            listner.onRowClick(data)
        }
    }

    interface Subjectcommunicator {
        fun onRowClick(data: SubjectItem)
    }
}