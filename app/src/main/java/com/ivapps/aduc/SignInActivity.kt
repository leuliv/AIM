package com.ivapps.aduc

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.ivapps.aduc.db.RetrofitClient
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class SignInActivity : AppCompatActivity() {

    var email = ""
    var pass = ""

    val PREF_NAME = "ADUC_PREFS"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        login_btn.setOnClickListener {
            email = login_email.editableText.toString()
            pass = login_password.editableText.toString()
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                login(email, pass)
            } else {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_LONG).show()
            }
        }

        si_gotosignup.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        si_forgotpass.setOnClickListener {

        }

    }

    private fun login(email: String, pass: String) {
        val call = RetrofitClient
            .getInstance()
            .api
            .signIn(email, pass)

        call.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@SignInActivity, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                try {
                    val obj = JSONObject(Gson().toJson(response.body()))
                    val s = obj.getString("status")
                    val dept = obj.getString("user_department")
                    val access = obj.getString("user_access")
                    when (s) {
                        "ok" -> {
                            Toast.makeText(
                                this@SignInActivity,
                                "Sign In successful",
                                Toast.LENGTH_SHORT
                            ).show()
                            val editor =
                                getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
                            editor.putString("user_email", email)
                            editor.putString("user_department", dept)
                            editor.putString("user_access", access)
                            editor.putInt("notifs", 0)
                            editor.putString("ip", "192.168.43.210")
                            editor.apply()
                            startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                            finish()
                        }
                        else -> Toast.makeText(
                            this@SignInActivity,
                            ">Error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: IOException) {
                    Toast.makeText(this@SignInActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }

        })
    }
}
