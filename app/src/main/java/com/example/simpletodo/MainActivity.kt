package com.example.simpletodo

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.simpletodo.fragments.LeaderboardFragment
import com.example.simpletodo.fragments.UserProfileFragment
import com.example.simpletodo.fragments.SelectionFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.ParseQuery
import com.parse.ParseUser
import java.text.SimpleDateFormat
import java.util.*

open class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()
        checkMode()

        val fragmentManager: FragmentManager = supportFragmentManager


        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener {
                item ->
                var fragmentToShow: Fragment? = null
//            fragmentToShow = SelectionFragment()
                when (item.itemId){

                R.id.action_home -> {
                    fragmentToShow = SelectionFragment()
                    Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                }
                R.id.action_leaderboard -> {
                    fragmentToShow = LeaderboardFragment()
                    Toast.makeText(this, "LeaderBoard", Toast.LENGTH_SHORT).show()
                }
                R.id.action_profile -> {
                    fragmentToShow = UserProfileFragment()
                    Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
                }
            }

            if(fragmentToShow != null){
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragmentToShow).commit()
            }

            true
            true
        }
        findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.action_home

    }

    private fun checkMode(){
        val query = ParseQuery.getQuery(Player::class.java)
        val user = ParseUser.getCurrentUser()
        var mode: Boolean = false

        query.include(Player.KEY_USER)
        query.whereEqualTo(Player.KEY_USER, user)

        query.findInBackground { detail, e ->
            if (e == null) {
                Log.d("Main", "Objects: $detail")
                for (element in detail) {

                    mode = element.getToggle()
                    if(mode){
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }else{
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }

                }

            }

        }
    }

    private  fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name: CharSequence = "MyNotification"
            val description = "My notification channel description"

            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel = NotificationChannel(MainActivity.CHANNEL_ID, name, importance)
            notificationChannel.description = description
            val notificationManager = getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private companion object{
        private const val CHANNEL_ID = "channel01"
    }

}