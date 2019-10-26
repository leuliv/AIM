package com.ivapps.aduc

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.ivapps.aduc.db.RetrofitClient
import com.ivapps.aduc.utils.Post
import kotlinx.android.synthetic.main.activity_view_post.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewPostActivity : AppCompatActivity() {

    var postID = 0
    val post = Post()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_post)

        postID = intent.extras!!.getInt("post_id")

        setSupportActionBar(viewpost_toolbar)
        supportActionBar.apply {
            this!!.setDisplayHomeAsUpEnabled(true)
            title = "Post $postID"
        }

        val call = RetrofitClient
            .getInstance()
            .api
            .getPost(postID)
        call.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@ViewPostActivity, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
                try {
                    val main = JSONObject(Gson().toJson(response.body()))

                    post.id = main.getInt("id")
                    post.poster_email = main.getString("poster_email")
                    post.poster_department = main.getString("poster_department")
                    post.poster_rank = main.getString("poster_rank")
                    post.post_title = main.getString("post_title")
                    post.post_content = main.getString("post_content")
                    post.post_date = main.getString("post_date")
                    post.post_type = main.getString("post_type")
                    post.status = main.getString("status")
                    post.paths = main.getString("paths")
                    post.post_time = main.getString("post_time")
                    post.poster_college = main.getString("poster_college")

                    post_title_text.text = post.post_title
                    post_uploader_text.text = post.poster_rank
                    post_timestamp_text.text = post.post_date
                    post_content_text.text = post.post_content

                } catch (e: Exception) {
                    Toast.makeText(this@ViewPostActivity, e.message, Toast.LENGTH_LONG).show()
                }
            }

        })



    }
}
