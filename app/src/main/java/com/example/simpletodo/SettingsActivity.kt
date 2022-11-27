package com.example.simpletodo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.parse.ParseUser


class SettingsActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val modeSwitch = findViewById<Switch>(R.id.darkModeSwitch)

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

        val recipient = "example@gmail.com"
        val subject = "Bug Report"

        val intent: Intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_EMAIL, recipient)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)

        intent.type = "message/rfc822"

        startActivity(Intent.createChooser(intent, "Choose Client"))
    }




}