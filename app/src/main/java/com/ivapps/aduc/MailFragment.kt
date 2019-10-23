package com.ivapps.aduc


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ivapps.aduc.adapters.MailsAdapter
import com.ivapps.aduc.adapters.PostsAdapter

class MailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mail, container, false)

        val mailRecycler = view.findViewById<RecyclerView>(R.id.mail_list)
        mailRecycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            view.context,
            RecyclerView.VERTICAL,
            false
        )

        val d1 = "This is a message, something with the app?"
        val d2 = "This is a message"
        val d3 = "This is a message, something with the app?"
        val d4 = "This is a message"
        val list = arrayOf(arrayOf("Yared Kassa",d1,"30 mins"),arrayOf("Tesfa",d2,"1 hr"),arrayOf("Abraham",d3,"5hrs"),arrayOf("Leul Hailu",d4,"1 day"))

        mailRecycler.adapter = MailsAdapter(view.context, list)

        return view
    }

}
