package com.example.simpletodo.fragments

import android.content.Intent
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

    val photoFileName = "pfp.jpg"

    lateinit var player: ParseUser

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

        val query: ParseQuery<Player> = ParseQuery.getQuery(Player::class.java)
        query.findInBackground(object : FindCallback<Player> {
            fun done(player: Player, e: ParseException?) {
                if (e != null) {
                    val username = player.getUsername()
                    val profilePic = player.getPImage()
                    val bio = player.getBio()
                    val totalPoints = player.getPoints()
                    val averageAccuracy = player.getAcc()
                    val averageSpeed = player.getSpeed()

                // query for last 3 played games

                }
            }

            override fun done(objects: MutableList<Player>?, e: ParseException?) {
                if (e != null) {
                    Log.e(TAG, "Error fetching posts")

                } }

        })
        profilePic = view.findViewById(R.id.profilePic)
        username = view.findViewById(R.id.username)
        bio = view.findViewById(R.id.bio)
        totalPoints = view.findViewById(R.id.userPointsTotal)
        averageSpeed = view.findViewById(R.id.userSpeed)
        averageAcc = view.findViewById(R.id.userAcc)


        profilePic.setOnClickListener {
            onPickPhoto()
        }
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