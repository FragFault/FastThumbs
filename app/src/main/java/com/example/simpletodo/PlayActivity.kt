package com.example.simpletodo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
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
    var category = "poetry"
    var competitive:Boolean = true
    var daily:Boolean = false

    //Set span text for prompt
    var spannable = SpannableStringBuilder(prompt)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        ParseObject.registerSubclass(GameResults::class.java)
        ParseObject.registerSubclass(Player::class.java)

        var tvTimeLeft = findViewById<TextView>(R.id.tvTimeLeft)
        var tvPrompt = findViewById<TextView>(R.id.tvPrompt)
        var tvErrors = findViewById<TextView>(R.id.tvErrors)

        val extras = intent.extras
        if (extras != null) {
            //The key arguments here must match that used in the other activity
            var extractedString = extras.getString("prompt")
            tvPrompt.text = extractedString
            prompt = extractedString!!
            spannable = SpannableStringBuilder(prompt)
            extractedString = extras.getString("category")
            category = extractedString!!
            var extractedBoolean = extras.getBoolean("isCompetitive")
            competitive = extractedBoolean
            extractedBoolean = extras.getBoolean("isDaily")
            daily = extractedBoolean!!
        }

        // Make first character highlighted for user to know where to type
        spannable.setSpan(BackgroundColorSpan(Color.YELLOW),
            0,
            1,
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        tvPrompt.text = spannable

        // Variable for keyboard
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val etKeyboardView = findViewById<EditText>(R.id.etKeyboardView)

        // Introduce keyboard to user to start typing the prompt
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

        tvPrompt.text = spannable
        tvErrors.text = "# Incorrect: " + numErrors

        timer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tvTimeLeft.text = "Seconds Left: " + millisUntilFinished / 1000
            }
            fun cancelClock() {
                this.cancel()
            }
            override fun onFinish() {
                tvTimeLeft.text = "STOP TYPING!"

                calculateResults()

                // Hide Keyboard if game is finished
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(etKeyboardView.windowToken, 0)

                if (competitive) {
                    // Only submit game results if mode is competitive
                    submitGameResults(points, speed, user, competitive, accuracy, daily, category)
                }
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
            // If user input matched expected character, move on to next character
            if((event.unicodeChar).toChar().toString() == prompt.substring(currentLetter,currentLetter+1)) {
                currentLetter += 1

                if((event.unicodeChar).toChar().toString() == " ") {
                    wordsPerMinute += 1
                }

                var tvPrompt = findViewById<TextView>(R.id.tvPrompt)

                spannable.setSpan(ForegroundColorSpan(Color.GREEN),
                    0, // start
                    currentLetter, // char the player is expected to type
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                )

                tvPrompt.text = spannable

                // If prompt is fully completed, finish game
                if(currentLetter == prompt.length) {
                    timer.cancel()

                    // Account for last word character typed
                    wordsPerMinute += 1

                    // Hide the keyboard after game finished
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    val etKeyboardView = findViewById<EditText>(R.id.etKeyboardView)
                    imm.hideSoftInputFromWindow(etKeyboardView.windowToken, 0)

                    calculateResults()

                    if (competitive) {
                        submitGameResults(points,speed,user, competitive, accuracy, daily, category)
                    }

                    goToResultsActivity()
                }
                else {
                    // Reset previously highlighted characters
                    spannable.setSpan(BackgroundColorSpan(Color.TRANSPARENT),
                        0,
                        currentLetter,
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                    )
                    // Highlight the character the user is expected to type
                    spannable.setSpan(BackgroundColorSpan(Color.YELLOW),
                        currentLetter,
                        currentLetter+1,
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                    )
                    tvPrompt.text = spannable
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
        if(daily) {
            points*=3
        }
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
            }
        }
        // If the game isn't competitive, then it doesn't count towards player stats
        if (competitive) {
            updateOverallPlayerStats()
        }
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
        intent.putExtra("isDaily", daily)
        intent.putExtra("speed", speed)
        intent.putExtra("accuracy", accuracy)
        startActivity(intent)
        finish()
    }

    companion object {
        const val TAG = "PlayActivity"
        lateinit var timer: CountDownTimer
    }
}