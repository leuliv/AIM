package com.ivapps.aduc


import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.ivapps.aduc.adapters.PostsAdapter
import com.ivapps.aduc.db.RetrofitClient
import com.ivapps.aduc.utils.Post
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {

    var postCount = 0

    var ctx: Context? = null

    val PREF_NAME = "ADUC_PREFS"
    var postRecycler: RecyclerView? = null
    var refreshLayout: SwipeRefreshLayout? = null
    var linearLayoutManager:LinearLayoutManager? = null
    private var firstVisibleInListview: Int = 0

    @TargetApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        ctx = view.context


        postRecycler = view.findViewById(R.id.posts_list)
        linearLayoutManager = LinearLayoutManager(
            view.context,
            RecyclerView.VERTICAL,
            false
        )

        postRecycler!!.layoutManager = linearLayoutManager

        refresh()

        val fab = view.findViewById<FloatingActionButton>(R.id.post_fab)
        fab.setOnClickListener {
            val dialog = AddPostDialog()
            val ft = fragmentManager!!.beginTransaction()
            dialog.show(ft, dialog.TAG)
        }

        refreshLayout = view.findViewById(R.id.postrefresh_layout)
        refreshLayout!!.setOnRefreshListener {
            refresh()
        }

        postRecycler!!.setOnScrollChangeListener() { v, i, i2, i3, i4 ->
            val h = linearLayoutManager!!.findFirstVisibleItemPosition()
            if (h == 0){
                fab.visibility = View.VISIBLE
            }else{
                fab.visibility = View.GONE
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    private fun refresh() {
        val prefs = ctx!!.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val dept = prefs.getString("user_department", null)

        val callC = RetrofitClient
            .getInstance()
            .api
            .postCount(dept)

        callC.enqueue(object : Callback<JsonObject> {
            override fun onResponse(callC: Call<JsonObject>, res: Response<JsonObject>) {
                if (res.isSuccessful) {
                    try {
                        val jsonObject = JSONObject(Gson().toJson(res.body()))
                        val r = jsonObject.getString("response")

                        postCount = if (r == "Done")
                            jsonObject.getInt("count")
                        else
                            0

                        if (postCount != 0) {
                            val call = RetrofitClient
                                .getInstance()
                                .api
                                .getPosts(dept)
                            call.enqueue(object : Callback<JsonObject> {
                                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                    Toast.makeText(ctx, t.message, Toast.LENGTH_LONG).show()
                                }

                                override fun onResponse(
                                    call: Call<JsonObject>,
                                    response: Response<JsonObject>
                                ) {
                                    try {
                                        val list = Array(postCount) { Post() }

                                        for (i in 0 until postCount) {
                                            val post = Post()
                                            val obj = JSONObject(Gson().toJson(response.body()))
                                            val main = obj.getJSONObject("post_${i}")

                                            post.id = main.getInt("id")
                                            post.poster_email = main.getString("poster_email")
                                            post.poster_department =
                                                main.getString("poster_department")
                                            post.poster_rank = main.getString("poster_rank")
                                            post.post_title = main.getString("post_title")
                                            post.post_content = main.getString("post_content")
                                            post.post_date = main.getString("post_date")
                                            post.post_type = main.getString("post_type")
                                            post.status = main.getString("status")
                                            post.paths = main.getString("paths")
                                            post.post_time = main.getString("post_time")
                                            post.poster_college = main.getString("poster_college")

                                            list[i] = post
                                        }

                                        postRecycler!!.adapter = PostsAdapter(ctx!!, list)
                                        firstVisibleInListview = linearLayoutManager!!.findFirstVisibleItemPosition()

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
                refreshLayout!!.isRefreshing = false
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(ctx, t.message, Toast.LENGTH_LONG).show()
                refreshLayout!!.isRefreshing = false
            }

        })
    }

}
