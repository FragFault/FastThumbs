package com.example.simpletodo

import android.R
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class ReminderBroadcast: BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        val builder: NotificationCompat.Builder? =
            p0?.let {
                NotificationCompat.Builder(it, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_menu_my_calendar)
                    .setContentTitle("Fast Thumbs")
                    .setContentText("Hey kids! There's a Daily challenge to complete")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            }

        val notificationManager = p0?.let { NotificationManagerCompat.from(it) }
        if (notificationManager != null) {
            if (builder != null) {
                notificationManager.notify(200, builder.build())
            }

        }
    }

//    private fun showNotification(){
//        createNotificationChannel()
//
//        val date = Date()
//        val notificationId = SimpleDateFormat("ddHHmmss", Locale.US).format(date).toInt()
//
//        val mainIntent = Intent(this, PlayDailyActivity::class.java)
//        //if you  want to pass data in notification and get in required activity
//        mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        val mainPendingIntent = PendingIntent.getActivity(this, 1, mainIntent, PendingIntent.FLAG_IMMUTABLE)
//
//        val notificationBuilder = NotificationCompat.Builder(this, "${SettingsActivity.CHANNEL_ID}")
//
//        notificationBuilder.setSmallIcon(R.drawable.logo)
//
//        notificationBuilder.setContentTitle("Fast Thumbs")
//
//        notificationBuilder.setContentText("Hey buddy don't forget to do the daily challenge huehuehue")
//
//        notificationBuilder.priority = NotificationCompat.PRIORITY_DEFAULT
//        //cancel notification on click
//        notificationBuilder.setAutoCancel(true)
//
//        notificationBuilder.setContentIntent(mainPendingIntent)
//
//        val notificationManagerCompat = NotificationManagerCompat.from(this)
//        notificationManagerCompat.notify(notificationId, notificationBuilder.build())
//    }
//
//    private  fun createNotificationChannel(){
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            val name: CharSequence = "MyNotification"
//            val description = "My notification channel description"
//
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//            val notificationChannel = NotificationChannel(SettingsActivity.CHANNEL_ID, name, importance)
//            notificationChannel.description = description
//            val notificationManager = getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(notificationChannel)
//        }
//    }
//
    private companion object{
        private const val CHANNEL_ID = "channel01"
    }
}