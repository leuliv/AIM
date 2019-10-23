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
import com.ivapps.aduc.helpers.GetUser
import com.ivapps.aduc.utils.User
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var mToggle: ActionBarDrawerToggle? = null
    var prevMenuItem: MenuItem? = null
    val PREF_NAME = "ADUC_PREFS"
    val user = User()

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
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.main_url)))
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
        val notificationsTab = main_bottomnav.findViewById<BottomNavigationItemView>(R.id.notif_menu)
        val badge = LayoutInflater.from(this).inflate(R.layout.component_tabbar_badge, notificationsTab, false)
        val t = badge.findViewById<TextView>(R.id.notificationsBadgeTextView)

        t.text = "99+"

        notificationsTab.addView(badge)
    }

    private fun setUpPrefs(){
        val prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        user.userEmail = prefs.getString("user_email",null)
    }

    override fun onStart() {
        super.onStart()
        refresh()
    }

    private fun refresh(){
        setUpPrefs()
        if (user.userEmail!=null){
            //GetUser(user.userEmail,this).setUp()
            setNotificationBadge()
        }else{
            startActivity(Intent(this,StartActivity::class.java))
            finish()
        }
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

    private fun signOut(){
        val editor = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
        editor.putString("user_email", null)
        editor.apply()
        refresh()
    }

}
