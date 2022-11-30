package com.example.simpletodo.fragments

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.simpletodo.BoardAdapter
import com.example.simpletodo.R
import com.example.simpletodo.SettingsActivity
import com.parse.ParseUser
import com.parse.*

/**
 * A simple [Fragment] subclass.
 * Use the [UserProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserProfileFragment : Fragment() {

    val PICK_PHOTO_CODE = 1046

    lateinit var profilePic: ImageView

    val photoFileName = "pfp.jpg"

    lateinit var user: ParseUser

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

        view.findViewById<TextView>(R.id.username).text = user.username

        profilePic = view.findViewById(R.id.profilePic)
        if (user.getParseFile(KEY_PFP) != null) {
            Glide.with(requireContext())
                .load(user.getParseFile(KEY_PFP)?.url)
                .transform(CircleCrop())
                .into(profilePic)
        } else {
            Glide.with(requireContext())
                .load(R.drawable.user_filled_24)
                .transform(CircleCrop())
                .into(profilePic)
        }

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
    }
}