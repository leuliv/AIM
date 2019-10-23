package com.ivapps.aduc

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ivapps.aduc.db.RetrofitClient
import com.ivapps.aduc.utils.User
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.sign_up_1.*
import kotlinx.android.synthetic.main.sign_up_2.*
import kotlinx.android.synthetic.main.sign_up_3.*
import kotlinx.android.synthetic.main.sign_up_4.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class SignUpActivity : AppCompatActivity() {

    var cur = 1
    val myCalendar = Calendar.getInstance()

    var job = ""
    var uname = ""
    var email = ""
    var pass = ""
    var retype = ""
    var gender = ""
    var bday = ""
    var phone = ""
    var college = ""
    var dept = ""

    val PREF_NAME = "ADUC_PREFS"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }

        pick_bday.setOnClickListener {
            DatePickerDialog(
                this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        fin_btn.setOnClickListener {
            val right = AnimationUtils.loadAnimation(this, R.anim.slide_right)
            val shake = AnimationUtils.loadAnimation(this, R.anim.shake)

            val user = User()

            if (cur == 1) {
                job = jobs_spinner.selectedItem.toString()
                if (job != "Select What You Do" && job.isNotEmpty()) {
                    v1.startAnimation(right)
                    v1.visibility = View.GONE

                    v2.startAnimation(right)
                    v2.visibility = View.VISIBLE
                    cur++
                } else {
                    jobs_spinner.startAnimation(shake)
                }
            } else if (cur == 2) {
                uname = username_edittext.editableText.toString()
                email = email_edittext.editableText.toString()
                pass = pass_edittext.editableText.toString()
                retype = retype_pass_edittext.editableText.toString()
                if (uname.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && retype.isNotEmpty() && pass == retype) {
                    v2.startAnimation(right)
                    v2.visibility = View.GONE

                    v3.startAnimation(right)
                    v3.visibility = View.VISIBLE
                    cur++
                } else {
                    if (uname.isEmpty()) {
                        error(username_edittext, "Username Empty")
                    }

                    if (email.isEmpty()) {
                        error(email_edittext, "Email Empty")
                    }

                    if (pass.isEmpty()) {
                        error(pass_edittext, "Password Empty")
                    }

                    if (retype.isEmpty()) {
                        error(retype_pass_edittext, "Field Empty")
                    }

                    if (pass != retype) {
                        error(retype_pass_edittext, "Passwords don't match")
                    }
                }
            } else if (cur == 3) {
                gender = gender_spinner.selectedItem.toString()
                bday = pick_bday.editableText.toString()
                phone = phone_edittext.editableText.toString()

                if (gender != "Select Gender" && gender.isNotEmpty() && bday.isNotEmpty() && phone.isNotEmpty()) {
                    v3.startAnimation(right)
                    v3.visibility = View.GONE

                    v4.startAnimation(right)
                    v4.visibility = View.VISIBLE
                    fin_btn.text = "Finish"
                    cur++
                } else {
                    if (gender == "Select Gender" || gender.isEmpty()) {
                        gender_spinner.startAnimation(shake)
                    }
                    if (bday.isEmpty()) {
                        error(pick_bday, "Birthday Empty")
                    }
                    if (phone.isEmpty()) {
                        error(phone_edittext, "Phone Empty")
                    }
                }


            } else if (cur == 4) {
                college = college_edittext.editableText.toString()
                dept = department_edittext.editableText.toString()

                if (college.isNotEmpty() && dept.isNotEmpty()) {
                    user.userJob = job
                    user.userName = uname
                    user.userEmail = email
                    user.userPassword = pass
                    user.userGender = gender
                    user.userBorn = bday
                    user.userPhone = phone.toInt()
                    user.userCollege = college
                    user.userDepartment = dept
                    reg(user)
                } else {
                    if (college.isEmpty()) {
                        error(college_edittext, "College Empty")
                    }

                    if (dept.isEmpty()) {
                        error(department_edittext, "Department Empty")
                    }
                }
            }
        }

        su_goto_signin.setOnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))
        }

    }

    private fun reg(user: User) {
        user.userSince = ""
        user.userAccess = ""
        user.userBio = ""
        user.userForgot = ""
        user.userProfile = "default"
        val call = RetrofitClient
            .getInstance()
            .api
            .signUp(user.userName,user.userEmail,user.userPhone,user.userCollege,user.userJob,user.userPassword,user.userProfile,user.userGender,user.userDepartment,user.userBorn,user.userForgot,user.userBio,user.userSince,user.userAccess)

        call.enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@SignUpActivity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                try {
                    val s = response.message()
                    when (s) {
                        "OK" -> {
                            Toast.makeText(this@SignUpActivity, "Account Created", Toast.LENGTH_SHORT).show()
                            val editor =
                                getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
                            editor.putString("user_email", user.userEmail)
                            editor.apply()
                            startActivity(Intent(this@SignUpActivity,MainActivity::class.java))
                            finish()
                        }
                        "exists" -> Toast.makeText(this@SignUpActivity, "account exists", Toast.LENGTH_SHORT).show()
                        else -> Toast.makeText(this@SignUpActivity, "Error", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: IOException) {
                    Toast.makeText(this@SignUpActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    fun error(view: EditText, error: String) {
        val shake = AnimationUtils.loadAnimation(this, R.anim.shake)
        view.error = error
        view.startAnimation(shake)
    }

    private fun updateLabel() {
        val myFormat = "dd/MM/yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.UK)

        pick_bday.setText(sdf.format(myCalendar.getTime()))
    }
    
}
