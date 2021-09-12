package com.aplus.edu.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.aplus.edu.R
import com.aplus.edu.api.response.StudymaterialItem
import com.aplus.edu.api.response.SubjectItem
import com.aplus.edu.constants.AppConstants
import com.makeramen.roundedimageview.RoundedImageView

class StudymaterialsAdapter internal constructor(
    private val context: Context,
    private val mData: List<StudymaterialItem>,
    val listner: Subjectcommunicator
) : RecyclerView.Adapter<StudymaterialsAdapter.ViewHolder>() {

    inner class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {


        internal var pdf_name_tv: TextView = itemView.findViewById(R.id.pdf_name_tv)
        internal var pdf_size_tv: TextView = itemView.findViewById(R.id.pdf_size_tv)
    }

    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.studymaterials_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mData[position]
        holder.pdf_name_tv.text = data.file
        holder.pdf_size_tv.text = data.fileSize
        holder.itemView.setOnClickListener {
            listner.onRowClick(data)
        }
    }

    interface Subjectcommunicator {
        fun onRowClick(data: StudymaterialItem)
    }
}