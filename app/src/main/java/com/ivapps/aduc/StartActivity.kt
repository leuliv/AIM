package com.ivapps.aduc

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        goto_signin.setOnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))
        }

        goto_signup.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
    }
}
