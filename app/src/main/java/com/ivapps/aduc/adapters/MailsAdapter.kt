package com.ivapps.aduc.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ivapps.aduc.R

class MailsAdapter internal constructor(
    private val context: Context, private val list:Array<Array<String>>
) :
    RecyclerView.Adapter<MailsAdapter.ViewHolder>() {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, p: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.single_mail_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, p: Int) {
        holder.senderNameView.text = list[p][0]
        holder.snapView.text = list[p][1]
        holder.timestampView.text = list[p][2]
    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val senderNameView: TextView = itemView.findViewById(R.id.sender_name)
        val snapView: TextView = itemView.findViewById(R.id.mail_snap)
        val timestampView: TextView = itemView.findViewById(R.id.mail_timestamp)
    }


}