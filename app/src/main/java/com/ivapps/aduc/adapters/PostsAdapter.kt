package com.ivapps.aduc.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ivapps.aduc.R

class PostsAdapter internal constructor(
    private val context: Context, private val list:Array<Array<String>>
) :
    RecyclerView.Adapter<PostsAdapter.ViewHolder>() {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, p: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.single_post_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, p: Int) {
        holder.titleView.text = list[p][0]
        holder.bodyView.text = list[p][1]
        holder.uploaderView.text = list[p][2]
        holder.timestampView.text = list[p][3]
    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleView: TextView = itemView.findViewById(R.id.post_title)
        val bodyView: TextView = itemView.findViewById(R.id.post_body)
        val uploaderView: TextView = itemView.findViewById(R.id.post_uploader)
        val timestampView: TextView = itemView.findViewById(R.id.post_timestamp)
    }


}