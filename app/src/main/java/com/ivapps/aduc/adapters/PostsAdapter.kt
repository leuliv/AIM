package com.ivapps.aduc.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ivapps.aduc.R
import com.ivapps.aduc.ViewPostActivity
import com.ivapps.aduc.utils.Post

class PostsAdapter internal constructor(
    private val context: Context, private val list:Array<Post>
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
        val post = list[p]


        holder.titleView.text = post.post_title
        holder.bodyView.text = post.post_content
        holder.uploaderView.text = post.poster_rank
        holder.timestampView.text = post.post_time

        holder.itemView.setOnClickListener{
            val intent = Intent(context,ViewPostActivity::class.java)
            intent.putExtra("post_id", post.id)
            context.startActivity(intent)
        }
    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleView: TextView = itemView.findViewById(R.id.post_title)
        val bodyView: TextView = itemView.findViewById(R.id.post_body)
        val uploaderView: TextView = itemView.findViewById(R.id.post_uploader)
        val timestampView: TextView = itemView.findViewById(R.id.post_timestamp)
    }


}