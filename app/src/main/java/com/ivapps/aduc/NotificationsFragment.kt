package com.ivapps.aduc


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.ivapps.aduc.adapters.MailsAdapter
import com.ivapps.aduc.adapters.NotificationsAdapter
import com.ivapps.aduc.db.RetrofitClient
import com.ivapps.aduc.utils.Notification
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationsFragment : Fragment() {

    var notifCount = 0

    var ctx: Context? = null

    val PREF_NAME = "ADUC_PREFS"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)
        ctx = view.context

        val notifRecycler = view.findViewById<RecyclerView>(R.id.notif_list)
        notifRecycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            view.context,
            RecyclerView.VERTICAL,
            false
        )

        val prefs = ctx!!.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val email = prefs.getString("user_email",null)

        val callC = RetrofitClient
            .getInstance()
            .api
            .notificationCount(email)

        callC.enqueue(object : Callback<JsonObject> {
            override fun onResponse(callC: Call<JsonObject>, res: Response<JsonObject>) {
                if (res.isSuccessful) {
                    try {
                        val jsonObject = JSONObject(Gson().toJson(res.body()))
                        val r = jsonObject.getString("response")

                        notifCount = if (r == "Done")
                            jsonObject.getInt("count")
                        else
                            0

                        val editor = ctx!!.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
                        editor.putInt("notifs", notifCount)
                        editor.apply()

                        if (notifCount != 0) {
                            val call = RetrofitClient
                                .getInstance()
                                .api
                                .getNotification(email)
                            call.enqueue(object : Callback<JsonObject> {
                                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                    Toast.makeText(ctx, t.message, Toast.LENGTH_LONG).show()
                                }

                                override fun onResponse(
                                    call: Call<JsonObject>,
                                    response: Response<JsonObject>
                                ) {
                                    try {
                                        val list = Array(notifCount) { Notification() }

                                        for (i in 0 until notifCount) {
                                            val notif = Notification()
                                            val obj = JSONObject(Gson().toJson(response.body()))
                                            val main = obj.getJSONObject("notif_${i}")

                                            notif.id = main.getInt("id")
                                            notif.userEmail = main.getString("user_email")
                                            notif.body = main.getString("body")
                                            notif.type = main.getString("type")
                                            notif.time = main.getString("time")
                                            notif.link = main.getString("link")
                                            notif.isSeen = main.getInt("seen") != 0

                                            list[i] = notif
                                        }
                                        notifRecycler.adapter = NotificationsAdapter(view.context, list)

                                    } catch (e: Exception) {
                                        Toast.makeText(ctx, e.message, Toast.LENGTH_LONG).show()
                                    }
                                }

                            })
                        }
                    } catch (e: Exception) {
                        Toast.makeText(ctx, e.message, Toast.LENGTH_LONG).show()
                    }

                } else {
                    Toast.makeText(ctx, "Error", Toast.LENGTH_LONG).show()
                }

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(ctx, t.message, Toast.LENGTH_LONG).show()
            }

        })

        return view
    }


}
