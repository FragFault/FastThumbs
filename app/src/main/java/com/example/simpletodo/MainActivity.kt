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
import android.widget.Switch
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.simpletodo.fragments.LeaderboardFragment
import com.example.simpletodo.fragments.UserProfileFragment
import com.example.simpletodo.fragments.SelectionFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.*

open class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

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