package com.ivapps.aduc.helpers

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import com.ivapps.aduc.db.RetrofitClient
import com.ivapps.aduc.utils.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import com.google.gson.reflect.TypeToken
import java.lang.Exception
import android.R.string
import org.json.JSONArray




class GetUser(val email: String,val ctx:Context) {

    val user = User()
    var isMe = false
    val PREF_NAME = "ADUC_PREFS"
    val prefs = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    private fun init() {
        isMe = prefs.getString("user_email",null) == email
    }

    fun setUp(){
        init()
        if (isMe) {
            val call = RetrofitClient
                .getInstance()
                .api.getUser(email)

            call.enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(ctx, t.message,Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    val l = response.message()
                    Toast.makeText(ctx, l,Toast.LENGTH_LONG).show()
                }

            })


            /*val editor = prefs.edit()
            editor.putString("id", user.id)
            editor.putString("user_name", user.userName)
            editor.putString("user_email", user.userEmail)
            editor.putInt("user_phone", user.userPhone)
            editor.putString("user_college", user.userCollege)
            editor.putString("user_job", user.userJob)
            editor.putString("user_password", user.userPassword)
            editor.putString("user_profile", user.userProfile)
            editor.putString("user_gender", user.userGender)
            editor.putString("user_department", user.userDepartment)
            editor.putString("user_born", user.userBorn)
            editor.putString("user_forgot", user.userForgot)
            editor.putString("user_bio", user.userBio)
            editor.putString("user_since", user.userSince)
            editor.putString("user_access", user.userAccess)
            editor.apply()*/
        }
    }

    fun user(): User {
        init()
        return user
    }
}