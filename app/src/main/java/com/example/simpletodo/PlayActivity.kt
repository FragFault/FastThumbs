package com.example.simpletodo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log

import android.os.CountDownTimer
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.parse.*
import com.example.simpletodo.fragments.OtherProfileFragment

class PlayActivity : AppCompatActivity() {

    // Variables created for play level
    var wordsPerMinute = 0
    var numErrors = 0
    var currentLetter = 0
    var points = 0
    var speed = 0
    var accuracy = 0
    val user = ParseUser.getCurrentUser()!!

    // Variables about game passed from selectLevel Screen
    var prompt = "test"
    val category = "poetry"
    val competitive:Boolean = true
    val daily:Boolean = false

    //Set span text for prompt
    var spannable = SpannableStringBuilder(prompt)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        val thePrompt = findViewById<TextView>(R.id.tvPrompt)
        val test = findViewById<TextView>(R.id.tvTitle)

        ParseObject.registerSubclass(GameResults::class.java)
        ParseObject.registerSubclass(Player::class.java)

        var tvTimeLeft = findViewById<TextView>(R.id.tvTimeLeft)
        var tvPrompt = findViewById<TextView>(R.id.tvPrompt)
        var tvErrors = findViewById<TextView>(R.id.tvErrors)


        val extras = intent.extras
        if (extras != null) {
            val value = extras.getString("prompt")
            val value2 = extras.getBoolean("isCompetetive")

            Log.i("PlayScreenInfo", value2.toString())

            //The key argument here must match that used in the other activity
//            test.text = value2.toString()

        // Introduce keyboard to user to start typing the prompt
            tvPrompt.text = value
            prompt = value!!
            spannable = SpannableStringBuilder(prompt)
        }

        // Variable for keyboard
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val etKeyboardView = findViewById<EditText>(R.id.etKeyboardView)

        // Introduce keyboard to user to start typing the prompt
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

        tvPrompt.text = spannable
        tvErrors.text = "# Incorrect: " + numErrors

        timer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tvTimeLeft.text = "Seconds Left: " + millisUntilFinished / 1000
            }
            fun cancelClock() {
                this.cancel()
            }
            override fun onFinish() {
                tvTimeLeft.text = "STOP TYPING!"
                Toast.makeText(this@PlayActivity, "OUT OF TIME!!!", Toast.LENGTH_SHORT).show()

                calculateResults()
                submitGameResults(points, speed, user, competitive, accuracy, daily, category)

                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(etKeyboardView.windowToken, 0)

                goToResultsActivity()
            }
        }.start()
    }

    // In case the user accidentally closes the keyboard, this onClickListener brings back the keyboard
    fun bringBackKeyboard(view: View) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (event != null) {
            if((event.unicodeChar).toChar().toString() == prompt.substring(currentLetter,currentLetter+1)) {
                currentLetter += 1

                if((event.unicodeChar).toChar().toString() == " ") {
                    wordsPerMinute += 1
                }

                spannable.setSpan(ForegroundColorSpan(Color.GREEN),
                    0, // start
                    currentLetter, // char the player is expected to type
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                )
                var tvPrompt = findViewById<TextView>(R.id.tvPrompt)
                tvPrompt.text = spannable

                // IF PROMPT IS FULLY COMPLETED, FINISH GAME
                if(currentLetter == prompt.length) {
                    timer.cancel()
                    Toast.makeText(this, "GAME OVER! Nice typing!", Toast.LENGTH_SHORT).show()

                    // Account for last word character typed
                    wordsPerMinute += 1

                    calculateResults()
                    submitGameResults(points,speed,user, competitive, accuracy, daily, category)

                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

                    val etKeyboardView = findViewById<EditText>(R.id.etKeyboardView)
                    imm.hideSoftInputFromWindow(etKeyboardView.windowToken, 0)

                    goToResultsActivity()
                }
            }
            else {
                // KeyCode 59 is capital case key, should not count towards score
                if(keyCode != 59) {
                    numErrors += 1
                    var tvErrors = findViewById<TextView>(R.id.tvErrors)
                    tvErrors.text = "# Incorrect: " + numErrors
                }
            }
        }
        return super.onKeyUp(keyCode, event)
    }

    fun calculateResults() {
        accuracy = ((currentLetter.toDouble()/(currentLetter+numErrors))*100).toInt()
        points = wordsPerMinute * accuracy
        speed = wordsPerMinute
    }

    // Send Results of Game to Database
    fun submitGameResults(points: Number, speed: Number, user: ParseUser,
                          competitive: Boolean, accuracy: Number, daily: Boolean, category: String) {
        val gameResults = GameResults()
        gameResults.setPoints(points)
        gameResults.setSpeed(speed)
        gameResults.setUser(user)
        gameResults.setCompetitive(competitive)
        gameResults.setAccuracy(accuracy)
        gameResults.setDaily(daily)
        gameResults.setCategory(category)
        gameResults.saveInBackground { exception ->
            if (exception != null) {
                // Something has gone wrong
                Toast.makeText(this, "Error: Didn't save to gameResults", Toast.LENGTH_SHORT).show()
                exception.printStackTrace()
            }
            else {
                // No errors
                //Toast.makeText(this, "SAVED TO DATABASE", Toast.LENGTH_SHORT).show()
            }
        }
        updateOverallPlayerStats()
    }

    fun updateOverallPlayerStats() {
        val query = ParseQuery.getQuery(Player::class.java)
        query.whereEqualTo(Player.KEY_USER, user)
        query.findInBackground { playerObjects , e ->
            if (e == null) {
                for (playerObject in playerObjects) {
                    val totalScore = (playerObject.getTotal())!!.toInt()
                    val totalGamesPlayed = playerObject.getGamesPlayed()
                    val avgAccuracy = ((playerObject.getAccuracy()!!.toInt() * (totalGamesPlayed!!.toInt() - 1) +accuracy) / totalGamesPlayed!!.toInt()).toInt()
                    val avgSpeed = ((playerObject.getSpeed()!!.toInt() * (totalGamesPlayed!!.toInt() - 1))+speed)/totalGamesPlayed!!.toInt()
                    playerObject.setTotal(points!!.toInt() + totalScore!!.toInt())
                    playerObject.setGamesPlayed(totalGamesPlayed!!.toInt() + 1)
                    playerObject.setAccuracy(avgAccuracy)
                    playerObject.setSpeed(avgSpeed)

                    playerObject.saveInBackground { exception ->
                        if (exception != null) {
                            // Something has gone wrong
                            Toast.makeText(this, "Error: Failed to save to Players table", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            // No error saving to Players table
                            // Toast.makeText(this, "USER POINTS SAVED TO DATABASE", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Error: Failure to query user stats in Players table", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToResultsActivity() {
        val intent = Intent(this@PlayActivity, ResultActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        const val TAG = "PlayActivity"
        lateinit var timer: CountDownTimer
    }
}