package com.example.simpletodo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.method.TextKeyListener.clear
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
import com.parse.*


class SettingsActivity : AppCompatActivity() {

//    private var dialogBuilder: AlertDialog.Builder? = null
//    lateinit var dialog: AlertDialog

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val fragmentManager: FragmentManager = supportFragmentManager
        val modeSwitch = findViewById<Switch>(R.id.darkModeSwitch)
        val settingsPage = findViewById<View>(R.id.SettingsPage)

        findViewById<ImageButton>(R.id.deleteButton).setOnClickListener {
            createNewContactDialog()
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

//            ParseUser.logOutInBackground { e ->
//                if (e == null) {
//                } else {
//                    e.printStackTrace()
//                }
//            }
//
//            ParseUser.getCurrentUser().deleteInBackground{ e ->
//                if (e == null) {
//                    // if the error is not null then we are displaying a toast message and opening our home activity.
//                    Toast.makeText(this@SettingsActivity, "Account Deleted", Toast.LENGTH_SHORT).show()
//                    val i = Intent(this@SettingsActivity, SignUpActivity::class.java)
//                    startActivity(i)
//                } else {
//                    // if we get error we are displaying it in below line.
//                    Toast.makeText(this@SettingsActivity, "Fail to delete account", Toast.LENGTH_SHORT).show()
//                }
//            }

//            query.whereEqualTo("user", Player.KEY_USER)
            query.include(Player.KEY_USER)
            query.findInBackground(object : FindCallback<Player> {
                override fun done(objects: MutableList<Player>?, e: ParseException?) {
                    if (e != null) {
                        Log.e("SettingsFragment:","Error fetching users")

                    } else {
                        if (objects != null){
                            Log.e("yo:",objects.size.toString())
                            Log.i("Test: ", "Parse User: " +  ParseUser.getCurrentUser() as String)
                            for (player in objects){
                                Log.i("Test: ", "Player list User: " +  player.getUser() as String)
//                                if (player.getUser() == ParseUser.getCurrentUser()){
//                                    Log.i("Settings: ", player.getAccuracy() as String)
//                                }
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

        val recipient = "example@gmail.com"
        val subject = "Bug Report"

        val intent: Intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_EMAIL, recipient)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)

        intent.type = "message/rfc822"

        startActivity(Intent.createChooser(intent, "Choose Client"))
    }




}