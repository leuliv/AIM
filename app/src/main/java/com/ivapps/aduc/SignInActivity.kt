package com.ivapps.aduc

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ivapps.aduc.db.RetrofitClient
import kotlinx.android.synthetic.main.activity_sign_in.*
import okhttp3.ResponseBody
import retrofit2.Call
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

        call.enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@SignInActivity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                try {
                    val s = response.message()
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
