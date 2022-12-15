package com.example.simpletodo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.parse.ParseQuery
import com.parse.ParseUser
import java.text.SimpleDateFormat
import java.util.*


class ResultActivity: AppCompatActivity() {
    val user = ParseUser.getCurrentUser()!!
    var dailyPlayed = false
    var speed = 0
    var accuracy = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.end_results)


        var speedRes = findViewById<TextView>(R.id.speedRes)
        var accRes = findViewById<TextView>(R.id.accRes)
        var dailyCText = findViewById<TextView>(R.id.dailyCText)

        val extras = intent.extras
        if (extras != null) {
            //The key arguments here must match that used in the other activity

            var extractedBoolean = extras.getBoolean("isDaily")
            dailyPlayed = extractedBoolean
            if (extractedBoolean) {
                dailyCText.text = "1/1"
            }

            checkIfDailyPlayed()
            if(dailyPlayed) {
                dailyCText.text = "1/1"
            }

            var extractedInt = extras.getInt("speed")
            speed = extractedInt
            speedRes.text = speed!!.toString() + "WPM"

            extractedInt = extras.getInt("accuracy")
            accuracy = extractedInt
            accRes.text = accuracy.toString() + "%"
        }
        else {
            Toast.makeText(this, "Error: Couldn't get played game results!", Toast.LENGTH_SHORT).show()
        }

        val conBtn = findViewById<Button>(R.id.conBtn).setOnClickListener {
            goToMainActivity()
        }
        val shareBtn = findViewById<Button>(R.id.shareBtn).setOnClickListener{
            shareOnTwitter()
        }
    }

    /*
    //query for all gamelogs testing
    fun queryPosts() {
        //specify which class to query in data base
        val query = ParseQuery.getQuery(GameResults::class.java)
        //find all gamelogs in our server
        query.whereEqualTo(GameResults.KEY_USER, currentUser)
        query.orderByDescending("updatedAt")
        query.setLimit(1)
        query.findInBackground { gamelogs , e ->
                if(e != null) {
                    //Something when wrong getting the game logs
                    Log.e(TAG, "Error getting Gamelogs")
                }else{
                    if (gamelogs != null ){
                        for (gamelog in gamelogs) {
                            Log.i(TAG, "gamelog: " + gamelog.getPoints())
                            findViewById<TextView>(R.id.speedRes).text = gamelog.getSpeed().toString() + "WPM"
                            findViewById<TextView>(R.id.accRes).text = gamelog.getAccuracy().toString() + "%"
                            if (gamelog.getDaily() == true){
                                findViewById<TextView>(R.id.dailyCText).text = "1/1"
                            }
                        }
                    }
                }
        }
        */

    fun checkIfDailyPlayed() {
        val sdf = SimpleDateFormat("M/dd/yyyy")
        var todaysDate = sdf.format(Date())
        todaysDate = todaysDate.toString()

        var parseDate = todaysDate.toString()

        val query = ParseQuery.getQuery(GameResults::class.java)
        query.whereEqualTo(GameResults.KEY_USER, user)
        query.orderByDescending("updatedAt")
        query.findInBackground { gamelogs, e ->
            if (e != null) {
                //Something when wrong getting the game logs
            } else {
                if (gamelogs != null) {
                    for (gamelog in gamelogs) {
                        parseDate = (sdf.format(gamelog.createdAt)).toString()
                        if (parseDate == todaysDate) {
                            if (gamelog.getDaily() == true) {
                                dailyPlayed = true
                                var dailyCText = findViewById<TextView>(R.id.dailyCText)
                                dailyCText.text = "1/1"
                                break
                            }
                        }
                        else {
                            break
                        }
                    }
                }
            }
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(this@ResultActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun shareOnTwitter() {
        val sendIntent = Intent(Intent.ACTION_SEND)
        val textSh = "FASTTHUMBS\nToday's type Session\nSpeed: " + findViewById<TextView>(R.id.speedRes).text.toString() + "\n" +
                "Accuracy: " + findViewById<TextView>(R.id.accRes).text.toString()
        sendIntent.putExtra(Intent.EXTRA_TEXT, textSh)
        sendIntent.type = "image/jpg"
        startActivity(Intent.createChooser(sendIntent, "Share with"))
    }

    companion object{
        const val TAG = "ResultsActivity"
    }

}