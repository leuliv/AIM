package com.ivapps.aduc

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.ivapps.aduc.db.RetrofitClient
import com.ivapps.aduc.utils.User
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity() {

    private var mToggle: ActionBarDrawerToggle? = null
    var prevMenuItem: MenuItem? = null
    val PREF_NAME = "ADUC_PREFS"
    val user = User()
    var notifs = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(main_toolbar)

        supportActionBar!!.apply {
            setDisplayShowCustomEnabled(true)
        }

        mToggle =
            ActionBarDrawerToggle(this, drawer_layout, main_toolbar, R.string.open, R.string.close)
        drawer_layout.addDrawerListener(mToggle!!)
        mToggle!!.syncState()

        main_viewpager.adapter = ViewPagerAdapter(supportFragmentManager)
        main_bottomnav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.mail_menu -> main_viewpager.currentItem = 0
                R.id.home_menu -> main_viewpager.currentItem = 1
                R.id.notif_menu -> main_viewpager.currentItem = 2
            }
            false
        }

        main_viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                refresh()
                when (position) {
                    0 -> supportActionBar!!.title = "Mail"
                    1 -> supportActionBar!!.title = "Timeline"
                    2 -> supportActionBar!!.title = "Notifications"
                }
            }

            override fun onPageSelected(position: Int) {
                if (prevMenuItem != null)
                    prevMenuItem!!.isChecked = false
                else
                    main_bottomnav.menu.getItem(0).isChecked = false

                main_bottomnav.menu.getItem(position).isChecked = true
                prevMenuItem = main_bottomnav.menu.getItem(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        main_viewpager.currentItem = 1
        main_bottomnav.menu.getItem(1).isChecked = true

        nav_drawer.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.profile_nav -> {
                    startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
                    true
                }
                R.id.settings_nav -> {
                    //startActivity(Intent(this@MainActivity, SettingsPrefActivity::class.java))
                    true
                }
                R.id.about_nav -> {
                    val dialog = AboutDialog()
                    val ft = supportFragmentManager.beginTransaction()
                    dialog.show(ft, dialog.TAG)
                    true
                }
                R.id.help_nav -> {
                    val dialog = HelpDialog()
                    val ft = supportFragmentManager.beginTransaction()
                    dialog.show(ft, dialog.TAG)
                    true
                }
                R.id.gotoweb_nav -> {
                    val browserIntent =
                        Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.main_url)))
                    startActivity(browserIntent)
                    true
                }
                R.id.logout_nav -> {
                    signOut()
                    true
                }
                else -> false
            }
        }
    }

    private fun setNotificationBadge() {
        setUpPrefs()
        if (notifs !=0) {
            val notificationsTab =
                main_bottomnav.findViewById<BottomNavigationItemView>(R.id.notif_menu)
            val badge = LayoutInflater.from(this)
                .inflate(R.layout.component_tabbar_badge, notificationsTab, false)
            val t = badge.findViewById<TextView>(R.id.notificationsBadgeTextView)
            t.text = notifs.toString()

            notificationsTab.addView(badge)
        }
    }

    private fun setUpPrefs() {
        val prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        user.userEmail = prefs.getString("user_email", null)
        user.userAccess = prefs.getString("user_access", null)
        notifs = prefs.getInt("notifs", 0)
    }

    override fun onStart() {
        super.onStart()
        refresh()
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    private fun refresh() {
        setUpPrefs()
        if (user.userEmail != null) {
            val call = RetrofitClient
                .getInstance()
                .api
                .getUser(user.userEmail)
            call.enqueue(object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    val prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                    val ip = prefs.getString("ip", null)
                    val r = "Failed to connect to /$ip:80"
                    Toast.makeText(this@MainActivity, "Failed To Connect", Toast.LENGTH_SHORT).show()
                    if (r == t.message){
                        val dialog = NoInternetDialog()
                        val ft = supportFragmentManager.beginTransaction()
                        dialog.show(ft, dialog.TAG)
                    }
                }

                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    try {
                        val main = JSONObject(Gson().toJson(response.body()))
                        val user = User()

                        user.userAccess = main.getString("user_access")

                        val editor = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
                        editor.putString("user_access", user.userAccess)
                        editor.apply()

                        setUpPrefs()

                        if (user.userAccess != "approved") {
                            addNotif("not_approved")
                        }else{
                            updateCount()
                        }

                    } catch (e: Exception) {
                        Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
                    }
                }

            })
        } else {
            startActivity(Intent(this@MainActivity, StartActivity::class.java))
            finish()
        }

    }


    private fun addNotif(type: String) {
        val body = if (type == "not_approved")
            "Your account is awaiting approval"
        else
            "This is another kind of notification"
        val link = if (type == "not_approved")
            "goToSettings"
        else
            "goToProfile"

        val day = Date().date
        val time = "${Date().hours}:${Date().hours}"
        val date = "$time $day"

        val call = RetrofitClient
            .getInstance()
            .api
            .setNotification(user.userEmail, body, type, date, link, false)

        call.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
                try {
                    val obj = JSONObject(Gson().toJson(response.body()))
                    val s = obj.getString("status")
                    when (s) {
                        "ok" -> {
                            Toast.makeText(this@MainActivity, "Notif Added", Toast.LENGTH_SHORT)
                                .show()
                            updateCount()
                        }
                        "exists" -> {
                            Toast.makeText(
                                this@MainActivity,
                                "Notif exists",
                                Toast.LENGTH_SHORT
                            ).show()
                            updateCount()
                        }
                        else -> Toast.makeText(
                            this@MainActivity,
                            "Error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: IOException) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun updateCount() {
        val c = RetrofitClient
            .getInstance()
            .api
            .notificationCount2(user.userEmail)

        c.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onResponse(
                call: Call<JsonObject>,
                res: Response<JsonObject>
            ) {
                val objc = JSONObject(Gson().toJson(res.body()))
                val count = objc.getInt("count")

                val editor =
                    getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
                notifs = count
                editor.putInt("notifs", count)
                editor.apply()
                setNotificationBadge()
                Toast.makeText(this@MainActivity,count.toString(),Toast.LENGTH_LONG).show()
            }

        })
    }


    inner class ViewPagerAdapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> MailFragment()
                1 -> HomeFragment()
                2 -> NotificationsFragment()
                else -> ErrorFragment()
            }
        }

        override fun getCount(): Int {
            return 3
        }
    }

    private fun signOut() {
        val editor = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
        editor.putString("user_email", null)
        editor.putString("user_department", null)
        editor.apply()
        refresh()
    }

}
