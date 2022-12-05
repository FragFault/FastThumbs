package com.example.simpletodo

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentManager
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser
import java.util.*


class SettingsActivity() : AppCompatActivity() {

//    private var dialogBuilder: AlertDialog.Builder? = null
//    lateinit var dialog: AlertDialog


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val fragmentManager: FragmentManager = supportFragmentManager
        val modeSwitch = findViewById<Switch>(R.id.darkModeSwitch)
        val settingsPage = findViewById<View>(R.id.SettingsPage)

        //        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
//            .setSmallIcon(R.drawable.logo)
//            .setContentTitle("FastThumbs")
//            .setContentText("You have yet to complete the Daily Challenge")
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        findViewById<ImageButton>(R.id.deleteButton).setOnClickListener {
            createNewContactDialog()
        }

        findViewById<Button>(R.id.notify_switch).setOnClickListener {
            Toast.makeText(this, "Reminder Set!", Toast.LENGTH_SHORT).show()
            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

            val timeAtButtonClick = System.currentTimeMillis();
            val tenSecondsInMillis = 1000 * 15;

            val intent = Intent(this@SettingsActivity, ReminderBroadcast::class.java)
            val pendingIntent: PendingIntent = PendingIntent.getBroadcast(this@SettingsActivity, 0, intent, PendingIntent.FLAG_IMMUTABLE)

            alarmManager.set(AlarmManager.RTC_WAKEUP, timeAtButtonClick + AlarmManager.INTERVAL_HALF_HOUR, pendingIntent)
        }


        findViewById<Switch>(R.id.darkModeSwitch).setOnClickListener {
            var modeSwitchState = modeSwitch.isChecked
            switchMode(modeSwitchState)
        }

        findViewById<Button>(R.id.logoutButton).setOnClickListener{
            logoutUser()
        }

        findViewById<ImageButton>(R.id.bugButton).setOnClickListener {
            sendBug()
        }

        findViewById<ImageButton>(R.id.goBack).setOnClickListener {

//            var fragmentToShow = UserProfileFragment()
//            fragmentManager.beginTransaction().replace(R.id.flContainer, fragmentToShow).commit()/


            this.onBackPressed()
        }
    }

    private fun createNewContactDialog(){
        val dialogBuilder = AlertDialog.Builder(this)
        val contactPopupView: View = layoutInflater.inflate(R.layout.popup, null)

        dialogBuilder!!.setView(contactPopupView)

        var dialog: AlertDialog = dialogBuilder!!.create()
        dialog.show()

        contactPopupView.findViewById<Button>(R.id.button2).setOnClickListener {
            val query: ParseQuery<Player> = ParseQuery.getQuery(Player::class.java)


//            query.whereEqualTo("user", Player.KEY_USER)
            query.include(Player.KEY_USER)
            query.findInBackground(object : FindCallback<Player> {
                override fun done(objects: MutableList<Player>?, e: ParseException?) {
                    if (e != null) {
                        Log.e("SettingsFragment:","Error fetching users")

                    } else {
                        if (objects != null){
//                            Log.e("yo:",objects.size.toString())
                            Log.e("yo: ", "Current User: " +  ParseUser.getCurrentUser().email.toString())
                            for (player in objects){
                                Log.i("yo: ", "Object User: " + player.getUser()?.email.toString())
                                if (player.getUser()?.email == ParseUser.getCurrentUser().email){
                                    Toast.makeText(this@SettingsActivity, "We did it. Its gone", Toast.LENGTH_SHORT).show()

                                    player.delete()

                                    ParseUser.getCurrentUser().deleteInBackground{ e ->
                                        if (e == null) {
                                            // if the error is not null then we are displaying a toast message and opening our home activity.
                                            Toast.makeText(this@SettingsActivity, "Account Deleted", Toast.LENGTH_SHORT).show()
                                            ParseUser.logOut()
                                            val i = Intent(this@SettingsActivity, SignUpActivity::class.java)
                                            startActivity(i)
                                        } else {
                                            // if we get error we are displaying it in below line.
                                            Toast.makeText(this@SettingsActivity, "Fail to delete account", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            }

                        }else{
                            // if we don't get the data in our database then we are displaying below message.
                            Toast.makeText(
                                this@SettingsActivity,
                                "Fail to get the object..",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                }
            })

        }

        contactPopupView.findViewById<Button>(R.id.button3).setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun switchMode(theme: Boolean) {
         if(theme){
             AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
         }else if(!theme){
             AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
         }
    }
    private fun logoutUser() {
        ParseUser.logOutInBackground { e ->
            if (e == null) {
                Log.i(LoginActivity.TAG, "Successfully logged out user")
                goToSignUpActivity()
            } else {
                e.printStackTrace()
               Toast.makeText(this, "Oops. Couldn't log out for some reason", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun goToSignUpActivity() {
        val intent = Intent(this@SettingsActivity, SignUpActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun sendBug(){ //FUNCTIONAL????

        val recipient = "Whatzituya2@gmail.com"
        val addresses = recipient.split(",".toRegex()).toTypedArray()
        val subject = "Bug Report"
        val message = "Hi, theres seems to be an issue with FastThumbs."

        val intent: Intent = Intent(Intent.ACTION_SEND)
//            data = Uri.parse("emailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, addresses)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, message)



        intent.type = "message/rfc822"
        if(intent.resolveActivity(packageManager) != null){
            startActivity(Intent.createChooser(intent, "Choose Client"))
        }

    }



}