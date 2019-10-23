package com.ivapps.aduc


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ivapps.aduc.adapters.MailsAdapter
import com.ivapps.aduc.adapters.NotificationsAdapter

class NotificationsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)

        val notifRecycler = view.findViewById<RecyclerView>(R.id.notif_list)
        notifRecycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            view.context,
            RecyclerView.VERTICAL,
            false
        )

        val d1 = "This is the first notification, something with the app?"
        val d2 = "This is the second notification"
        val d3 = "This is the third notification, something with the app?"
        val d4 = "This is the fourth notification"
        val list = arrayOf(arrayOf(d1,"30 mins"),arrayOf(d2,"1 hr"),arrayOf(d3,"5hrs"),arrayOf(d4,"1 day"))

        notifRecycler.adapter = NotificationsAdapter(view.context, list)

        return view
    }


}
