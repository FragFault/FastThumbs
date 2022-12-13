package com.example.simpletodo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.parse.ParseFile
import com.parse.ParseQuery
import com.parse.ParseUser

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

//        if (ParseUser.getCurrentUser() != null) {
//            goToMainActivity()
//        }

        findViewById<TextView>(R.id.login_button).setOnClickListener {
            goToLoginActivity()
        }
        findViewById<Button>(R.id.signUp_button).setOnClickListener {
            val username = findViewById<EditText>(R.id.et_username).text.toString()
            val password = findViewById<EditText>(R.id.et_password).text.toString()
            val email = findViewById<EditText>(R.id.et_email).text.toString()
            signUpUser(username, password, email)
        }
    }
    private fun signUpUser(username: String, password: String, email: String) {
        val user = ParseUser()

        user.setUsername(username)
        user.setEmail(email)
        user.setPassword(password)

        user.signUpInBackground { e ->
            if (e == null) {
                // Created a new account!
                Toast.makeText(this, "Successfully signed up!", Toast.LENGTH_SHORT).show()
                val newuser = Player()
                newuser.setGamesPlayed(0)
                newuser.setToggle(false)
                newuser.setSpeed(0)
                newuser.setTotal(0)
                newuser.setBio("Hello I am a New User!")
                newuser.setUser(ParseUser.getCurrentUser())
                newuser.setAccuracy(0)
                newuser.saveInBackground { exception ->
                    if (exception != null) {
                        // Something has gone wrong
                        Log.e("yo:",exception.toString())
                        Toast.makeText(this, "Error: Didn't save user", Toast.LENGTH_SHORT).show()
                        exception.printStackTrace()
                    }
                    else {
                        // No errors
                        //Toast.makeText(this, "SAVED TO DATABASE", Toast.LENGTH_SHORT).show()
                    }
                }
                goToMainActivity()
            } else {
                e.printStackTrace()
                Toast.makeText(this, "Error: Sign up failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun goToMainActivity() {
        val intent = Intent(this@SignUpActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun goToLoginActivity() {
        val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
    companion object {
        const val TAG = "SignUpActivity"
    }
}