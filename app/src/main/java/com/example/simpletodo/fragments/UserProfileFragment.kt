package com.example.simpletodo.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.simpletodo.Player
import com.example.simpletodo.R
import com.example.simpletodo.SettingsActivity
import com.parse.ParseUser
import com.parse.*
import android.widget.Toast
import com.example.simpletodo.GameLog
import java.io.IOException

/**
 * A simple [Fragment] subclass.
 * Use the [UserProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserProfileFragment : Fragment() {

    val PICK_PHOTO_CODE = 1046

    lateinit var profilePic: ImageView
    lateinit var username: TextView
    lateinit var bio: EditText
    lateinit var averageSpeed: TextView
    lateinit var averageAcc: TextView
    lateinit var totalPoints: TextView
    lateinit var user : ParseUser

    val photoFileName = "pfp.jpg"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<ImageButton>(R.id.settingsButton)
            .setOnClickListener(View.OnClickListener {
                val intent = Intent(context, SettingsActivity::class.java)
                context?.startActivity(intent)
            })

        super.onViewCreated(view, savedInstanceState)
        user = ParseUser.getCurrentUser()

//        val query: ParseQuery<Player> = ParseQuery.getQuery(Player::class.java)
//        query.include(Player.KEY_USER)
//        query.whereEqualTo(Player.KEY_USER, user.objectId)
//        query.findInBackground(object : FindCallback<Player> {
//                        override fun done(objects: MutableList<Player>?, e: ParseException?) {
//                if (e == null) {
////                    Log.e(TAG, "Error fetching posts in if")
//
//                    if (objects != null) {
//                        for (player in objects) {
//                            Log.d("LeaderboardFragment:","Player:"+player.getUsername()!!)
//                            val username = player.getUsername()
//                            val profilePic = player.getPImage()
//                            val bio = player.getBio()
//                            val totalPoints = player.getPoints()
//                            val averageAccuracy = player.getAcc()
//                            val averageSpeed = player.getSpeed()
//                        }
//                    }
//                } else {
//                    if (objects != null) {
//                    Log.e(TAG, "Error fetching posts in else")
//                        for (player in objects) {
////                            Log.i("LeaderboardFragment:","Player:"+player.getUsername()!!)
////                            val username = player.getUsername()
////                            val profilePic = player.getPImage()
////                            val bio = player.getBio()
////                            val totalPoints = player.getPoints()
////                            val averageAccuracy = player.getAcc()
////                            val averageSpeed = player.getSpeed()
//                        }
//
//
//                    }}
//                        }
//
//
//        })
        profilePic = view.findViewById(R.id.profilePic)
        username = view.findViewById(R.id.username)
        bio = view.findViewById(R.id.bio)
        totalPoints = view.findViewById(R.id.userPointsTotal)
        averageSpeed = view.findViewById(R.id.userSpeed)
        averageAcc = view.findViewById(R.id.userAcc)
        Log.d(TAG, user.objectId)
        profiledetails()


        profilePic.setOnClickListener {
            onPickPhoto()
        }
    }

    fun profiledetails() {
        val query = ParseQuery.getQuery(Player::class.java)
        query.include(Player.KEY_USER)
        query.whereEqualTo(Player.KEY_USER, user)
        query.findInBackground { detail , e ->
            if (e == null) {
                Log.d(Companion.TAG, "Objects: $detail")
                for (element in detail) {
                    Log.i(TAG, "this is the bio" + element.getBio().toString())
                    element.getBio()

                    username.setText(user.username.toString())
//                     profilePic.setImageURI(loadFromUri(element.getPImage()))
                    bio.setText(element.getBio()).toString()
                    totalPoints.setText(element.getPoints().toString())
                    averageAcc.setText(element.getAcc().toString())
                    averageSpeed.setText(element.getSpeed().toString())

                }
            } else {
                Log.e(Companion.TAG, "Parse Error: ", e)
            }
        }
        Log.e(TAG, "This works")

    }

//    fun gamedetails() {
//        val query = ParseQuery.getQuery(GameLog::class.java)
//        query.include(GameLog.KEY_USER)
//        query.whereEqualTo(GameLog.KEY_USER, user.objectId)
//        query.findInBackground { detail, e ->
//            if (e == null) {
//                Log.d(TAG, "Objects: $detail")
//                for (element in detail) {
//                    Log.i(TAG, "this is the bio" + element.getDate().toString())
//
////                     profilePic.setImageURI(loadFromUri(element.getPImage()))
//                    bio.setText(element.getDate()).toString()
//                    totalPoints.setText(element.getPoints().toString())
//                    averageAcc.setText(element.getAcc().toString())
//                    averageSpeed.setText(element.getSpeed().toString())
//
//                }
//            } else {
//                Log.e(Companion.TAG, "Parse Error: ", e)
//            }
//        }
//        Log.e(TAG, "This works")
//    }


        fun loadFromUri(photoUri: Uri?): Bitmap? {
        var image: Bitmap? = null
        try {
            // check version of Android on device
            if (Build.VERSION.SDK_INT > 27 && photoUri != null) {
                // on newer versions of Android, use the new decodeBitmap method
                val source: ImageDecoder.Source =
                    ImageDecoder.createSource(requireContext().getContentResolver(), photoUri)
                image = ImageDecoder.decodeBitmap(source)
            } else {
                // support older versions of Android by using getBitmap
                image = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), photoUri)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return image
    }

    fun onPickPhoto() {
        // Create intent for picking a photo from the gallery
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(intent, PICK_PHOTO_CODE)
        }
    }

    companion object {
            const val KEY_PFP = "profilePic"
        private const val TAG = "ProfileFragment"
    }
}