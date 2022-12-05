package com.example.simpletodo.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.simpletodo.*
import com.parse.ParseQuery
import com.parse.ParseUser


/**
 * A simple [Fragment] subclass.
 * Use the [OtherProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OtherProfileFragment : Fragment() {


    val PICK_PHOTO_CODE = 1046

    lateinit var profilePic: ImageView
    lateinit var username: TextView
    lateinit var bio: TextView
    lateinit var averageSpeed: TextView
    lateinit var averageAcc: TextView
    lateinit var totalPoints: TextView
    lateinit var user : ParseUser
    lateinit var GameLogRecyclerView: RecyclerView
    lateinit var adapter: GameLogAdapter
    var allGameLogs: MutableList<GameResults> = mutableListOf()


    val photoFileName = "pfp.jpg"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_other_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<ImageButton>(R.id.backbutton)
            .setOnClickListener(View.OnClickListener {
                val intent = Intent(context, LeaderboardFragment::class.java)
                context?.startActivity(intent)
            })

        super.onViewCreated(view, savedInstanceState)
        user = ParseUser.getCurrentUser()

        profilePic = view.findViewById(R.id.profilePic2)
        username = view.findViewById(R.id.username2)
        bio = view.findViewById(R.id.bio2)
        totalPoints = view.findViewById(R.id.userPointsTotal2)
        averageSpeed = view.findViewById(R.id.userSpeed2)
        averageAcc = view.findViewById(R.id.userAcc2)
        GameLogRecyclerView = view.findViewById(R.id.gamedetails)
        adapter = GameLogAdapter(requireContext(),allGameLogs)
        GameLogRecyclerView.adapter = adapter
        GameLogRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        Log.d(TAG, user.objectId)
        profiledetails()
        queryGameLogs()
    }

    fun profiledetails() {
        val query = ParseQuery.getQuery(Player::class.java)
        query.include(Player.KEY_USER)
        query.whereEqualTo(Player.KEY_USER, user)
        query.findInBackground { detail , e ->
            if (e == null) {
                Log.d(TAG, "Objects: $detail")
                for (element in detail) {
                    Log.i(TAG, "this is the bio" + element.getBio().toString())
                    element.getBio()

                    username.setText(user.username.toString())
                    if (element.getPImage() != null) {
                        Glide.with(requireContext())
                            .load(element.getPImage()?.url)
                            .transform(CircleCrop())
                            .into(profilePic)
                    } else {
                        Glide.with(requireContext())
                            .load(R.drawable.instagram_user_filled_24)
                            .transform(CircleCrop())
                            .into(profilePic)
                    }
                    bio.setText(element.getBio()).toString()
                    totalPoints.setText(element.getPoints().toString())
                    averageAcc.setText(element.getAcc().toString()+"%")
                    averageSpeed.setText(element.getSpeed().toString()+" WPM")

                }
            } else {
                Log.e(TAG, "Parse Error: ", e)
            }
        }
        Log.e(TAG, "This works")

    }

    fun queryGameLogs(){
        val query = ParseQuery.getQuery(GameResults::class.java)
        query.include(GameResults.KEY_USER)
        query.whereEqualTo(GameResults.KEY_USER, user)
        query.findInBackground{ detail, e ->
            if (e == null) {
                Log.d(TAG, "Objects: $detail")
                for (element in detail) {
//                    Log.i(TAG, "this is the bio" + element.getDate().toString())

                    allGameLogs.addAll(detail)
                    adapter.notifyDataSetChanged()
                }
            }else {
                Log.i("Fragment:","GameLog is null")
            }
        }


    }
    companion object {
        const val KEY_PFP = "profilePic"
        private const val TAG = "OtherProfileFragment"
    }

}