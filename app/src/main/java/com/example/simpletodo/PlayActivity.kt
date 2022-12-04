package com.example.simpletodo

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class PlayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        val thePrompt = findViewById<TextView>(R.id.tvPrompt)

        val extras = intent.extras
        if (extras != null) {
            val value = extras.getString("prompt")
            //The key argument here must match that used in the other activity
            thePrompt.text = value
        }

        // Introduce keyboard to user to start typing the prompt
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    // Incase the user accidently closes the keyboard, this onClickListener brings back the keyboard
    fun bringBackKeyboard(view: View) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
}