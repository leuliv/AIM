package com.ivapps.aduc


import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ivapps.aduc.adapters.PostsAdapter
import com.ivapps.aduc.helpers.CheckNetworkStatus
import com.ivapps.aduc.helpers.HttpJsonParser
import org.json.JSONArray
import org.json.JSONException
import android.content.Intent.getIntent
import android.os.AsyncTask.execute
import java.lang.Exception


class HomeFragment : Fragment() {

    var postCount = 0

    var ctx:Context?=null

    private val KEY_SUCCESS = "success"
    private val KEY_DATA = "post_content"
    private val KEY_POST_ID = "id"
    private val KEY_POST_NAME = "post_title"
    private val BASE_URL = "http://192.168.137.1/aduc/"
    private var movieList: ArrayList<HashMap<String, String>>? = null
    private var movieListView: ListView? = null
    private var pDialog: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        ctx = view.context

        val postRecycler = view.findViewById<RecyclerView>(R.id.posts_list)
        postRecycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            view.context,
            RecyclerView.VERTICAL,
            false
        )
        val d1 = "Secondary line text Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam massa quam. Secondary line text Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam massa quam. Secondary line text Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam massa quam. Secondary line text Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam massa quam."
        val d2 = "Secondary line text Lorem ipsum dolor sit amet, consectetur adipiscing elit."
        val d3 = "Secondary line text Lorem ipsum dolor sit amet."
        val d4 = "Secondary line text Lorem ipsum dolor."
        val list = arrayOf(arrayOf("Title 1",d1,"Department","30 mins"),arrayOf("Title 2",d2,"University","1 hr"),arrayOf("Title 3",d3,"Department","5hrs"),arrayOf("Title 4",d4,"University","1 day"))

        postRecycler.adapter = PostsAdapter(view.context, list)

        return view
    }
}
