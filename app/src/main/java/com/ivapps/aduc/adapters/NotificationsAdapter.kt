package com.ivapps.aduc.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.ivapps.aduc.R
import com.ivapps.aduc.db.RetrofitClient
import com.ivapps.aduc.helpers.GetTime
import com.ivapps.aduc.utils.Notification
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationsAdapter internal constructor(
    private val context: Context, private val list:Array<Notification>
) :
    RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, p: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.single_notif_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, p: Int) {
        val n = list[p]
        holder.bodyView.text = n.body
        holder.timestampView.text = n.time
        holder.itemView.setOnClickListener {
            val call = RetrofitClient
                .getInstance()
                .api
                .updateNotification(n.id)
            call.enqueue(object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    try {
                        val main = JSONObject(Gson().toJson(response.body()))

                        val s = main.getString("status")

                        Toast.makeText(context,s + " " +n.link,Toast.LENGTH_LONG).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    }
                }

            })
        }
    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bodyView: TextView = itemView.findViewById(R.id.notif_txt)
        val timestampView: TextView = itemView.findViewById(R.id.notif_timestamp)
    }


}