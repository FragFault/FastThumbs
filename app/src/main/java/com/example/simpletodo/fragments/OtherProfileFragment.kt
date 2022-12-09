package com.example.simpletodo.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.simpletodo.R
import android.content.Intent
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.simpletodo.*
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser


/**
 * A simple [Fragment] subclass.
 * Use the [OtherProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OtherProfileFragment : Fragment() {

    lateinit var profilePic: ImageView
    lateinit var otherusername: TextView
    lateinit var bio: TextView
    lateinit var averageSpeed: TextView
    lateinit var averageAcc: TextView
    lateinit var totalPoints: TextView
//    lateinit var user : ParseUser
    lateinit var message : String
    lateinit var message2 : String
    lateinit var GameLogRecyclerView: RecyclerView
    lateinit var adapter: GameLogAdapter
    var allGameLogs: MutableList<GameResults> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bundle = arguments
        Log.i(TAG, "value of bundle" + bundle)
        message = bundle!!.getString("userId").toString()
        message2 = bundle!!.getString("username").toString()
        Log.i(TAG, "value of message" + message)

//
//       val theMessage = view?.findViewById<TextView>(R.id.username)
//
//        if (theMessage != null) {
//            theMessage.text = message as String
//        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_other_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val fragObj = LeaderboardFragment()

        view.findViewById<ImageButton>(R.id.backbutton)
            .setOnClickListener(View.OnClickListener {
                (context as FragmentActivity).supportFragmentManager.beginTransaction().replace(R.id.flContainer, fragObj).commit()
            })

        super.onViewCreated(view, savedInstanceState)
//        user = ParseUser.getCurrentUser()

        profilePic = view.findViewById(R.id.profilePic2)
        otherusername = view.findViewById(R.id.username2)
        bio = view.findViewById(R.id.bio2)
        totalPoints = view.findViewById(R.id.userPointsTotal2)
        averageSpeed = view.findViewById(R.id.userSpeed2)
        averageAcc = view.findViewById(R.id.userAcc2)
        GameLogRecyclerView = view.findViewById(R.id.gamedetails)
        adapter = GameLogAdapter(requireContext(),allGameLogs)
        GameLogRecyclerView.adapter = adapter
        GameLogRecyclerView.layoutManager = LinearLayoutManager(requireContext())


//        Log.d(TAG, user.objectId)
        profiledetails()
        queryGameLogs()
    }

    fun profiledetails() {
        val query = ParseQuery.getQuery(Player::class.java)
        query.include(message)
        query.whereContains(Player.KEY_USER, message)
        query.findInBackground{ detail, e ->


            if (e == null) {
                Log.d(TAG, "Objects: $detail")
                for (element in detail) {
                    Log.i(TAG, "this is the bio " + element.getBio().toString())
                    element.getBio()

                    otherusername.setText(message2)
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
                    totalPoints.setText(element.getTotal().toString())
                    averageAcc.setText(element.getAccuracy().toString()+"%")
                    averageSpeed.setText(element.getSpeed().toString()+" WPM")

                }
            } else {
                Log.e(TAG, "Parse Error: ", e)
            }
        }
        Log.e(TAG, Player.KEY_USER)

    }

    fun queryGameLogs(){
        val query = ParseQuery.getQuery(GameResults::class.java)
        query.include(message)
        query.orderByDescending("createdAt")
        query.whereContains(GameResults.KEY_USER, message)
        query.findInBackground( object: FindCallback<GameResults> {
            override fun done(detail: MutableList<GameResults>?, e: ParseException?) {
                Log.i(TAG, "value of size " + detail?.size)
                if (e == null) {
                    Log.d(TAG, "Objects: $detail")
//                    for (element in detail) {
////                    Log.i(TAG, "this is the bio" + element.getDate().toString())
//
//                        allGameLogs.addAll(detail)
//                        adapter.notifyDataSetChanged()
//                    }
                    if (detail?.size!! > 3) {
                        allGameLogs.addAll(detail.slice(0 .. 2 ))
                        adapter.notifyDataSetChanged()
                    } else if (detail?.size!! > 2) {
                        allGameLogs.addAll(detail.slice(0 .. 1 ))
                        adapter.notifyDataSetChanged()
                    } else if (detail?.size!! > 1) {
                        allGameLogs.addAll(detail.slice(0 .. 0 ))
                        adapter.notifyDataSetChanged()
                    }
                    else if (detail?.size!! == 1) {
                        allGameLogs.addAll(detail.slice(0 .. 0 ))
                        adapter.notifyDataSetChanged()
                    }else {
                        Toast.makeText(requireContext(), "Play a game to witness your own success!", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Log.i("Fragment:", "GameLog is null")
                }
            }

        })


    }
    companion object {
        const val KEY_PFP = "profilePic"
        private const val TAG = "OtherProfileFragment"
    }

}