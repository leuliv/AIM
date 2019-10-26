package com.ivapps.aduc

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.ivapps.aduc.db.RetrofitClient
import com.ivapps.aduc.utils.User
import kotlinx.android.synthetic.main.activity_profile.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    val PREF_NAME = "ADUC_PREFS"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setSupportActionBar(profile_toolbar)
        supportActionBar.apply {
            this!!.setDisplayHomeAsUpEnabled(true)
            title = "Me"
        }

        val prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val email = prefs.getString("user_email",null)

        val call = RetrofitClient
            .getInstance()
            .api
            .getUser(email)
        call.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@ProfileActivity, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
                try {
                    val main = JSONObject(Gson().toJson(response.body()))
                    val user = User()

                    user.id = main.getInt("id")
                    user.userName = main.getString("user_name")
                    user.userEmail = main.getString("user_email")
                    user.userPhone = main.getInt("user_phone")
                    user.userCollege = main.getString("user_college")
                    user.userJob = main.getString("user_job")
                    user.userProfile = main.getString("user_profile")
                    user.userGender = main.getString("user_gender")
                    user.userDepartment = main.getString("user_department")
                    user.userBorn = main.getString("user_born")
                    user.userForgot = main.getString("user_forgot")
                    user.userBio = main.getString("user_bio")
                    user.userSince = main.getString("user_since")
                    user.userAccess = main.getString("user_access")

                    profile_username.text = user.userName
                    profile_bio.text = user.userBio
                    profile_phone.text = user.userPhone.toString()
                    profile_gender.text = user.userGender
                    profile_bday.text = user.userBorn
                    profile_college.text = user.userCollege
                    profile_dept.text = user.userDepartment

                } catch (e: Exception) {
                    Toast.makeText(this@ProfileActivity, e.message, Toast.LENGTH_LONG).show()
                }
            }

        })


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.edit_profile_pic -> {

            }
            R.id.edit_name_tool -> {

            }
        }
        return true
    }
}
