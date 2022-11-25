package com.example.simpletodo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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