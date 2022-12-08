package com.example.simpletodo

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.parse.ParseQuery
import com.parse.ParseUser


class ResultActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.end_results)

        val conBtn = findViewById<Button>(R.id.conBtn).setOnClickListener {
            goToMainActivity()
        }

        queryPosts()
    }

    //query for all gamelogs testing
    fun queryPosts() {
        //gets current user
        val currentUser = ParseUser.getCurrentUser()
        if (currentUser != null) {
            Log.i(TAG, "currentUser: " + currentUser.objectId )
        } else {
            Log.i(TAG, "There was an error retrieving current user")
        }

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

    }

    private fun goToMainActivity() {
        val intent = Intent(this@ResultActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun shareOnTwitter() {
        val pm = packageManager
        try {
            val waIntent = Intent(Intent.ACTION_SEND)
            waIntent.type = "text/plain"
            val text = "Insert Tweet Here"
            val info = pm.getPackageInfo("com.twitter.android", PackageManager.GET_META_DATA)
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.twitter.android")
            waIntent.putExtra(Intent.EXTRA_TEXT, text)
            startActivity(Intent.createChooser(waIntent, "Share with"))
        } catch (e: PackageManager.NameNotFoundException) {
            Toast.makeText(this, "Twitter not Installed", Toast.LENGTH_SHORT)
                .show()
            return
        }
        return
    }

    companion object{
        const val TAG = "ResultsActivity"
    }

}