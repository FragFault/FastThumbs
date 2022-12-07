package com.example.simpletodo.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.simpletodo.BoardAdapter
import com.example.simpletodo.Player
import com.example.simpletodo.R
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import java.sql.Types.NULL

/**
 * A simple [Fragment] subclass.
 * Use the [LeaderboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LeaderboardFragment : Fragment() {
    lateinit var boardRecyclerView: RecyclerView
    lateinit var adapter: BoardAdapter

    var allPlayers: MutableList<Player> = mutableListOf()
    var threePlayers: MutableList<Player> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_leaderboard,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        boardRecyclerView = view.findViewById(R.id.BoardView)
        adapter = BoardAdapter(requireContext(),allPlayers)
        boardRecyclerView.adapter = adapter
        boardRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val firstPicture = view.findViewById<ImageView>(R.id.firstplace)
        val firstName = view.findViewById<TextView>(R.id.textView4)
        val firstPoints = view.findViewById<TextView>(R.id.textView5)
        val secondPicture = view.findViewById<ImageView>(R.id.imageView7)
        val secondName = view.findViewById<TextView>(R.id.textView8)
        val secondPoints = view.findViewById<TextView>(R.id.textView9)
        val thirdPicture = view.findViewById<ImageView>(R.id.imageView8)
        val thirdName = view.findViewById<TextView>(R.id.textView10)
        val thirdPoints = view.findViewById<TextView>(R.id.textView11)
        queryBoard(firstPicture,secondPicture,thirdPicture,firstName,firstPoints,secondName,secondPoints,thirdName,thirdPoints)
        Log.e("Leaderboardfragment:",""+threePlayers.size)
        // val firstPicture = view.findViewById<ImageView>(R.id.firstplace)
        //Glide.with(firstPicture.context).load(list.get(0).getPImage()?.url).circleCrop().into(firstPicture)
    }

    fun queryBoard(firstPicture: ImageView, secondPicture: ImageView, thirdPicture: ImageView, firstName: TextView, firstPoints: TextView, secondName: TextView, secondPoints: TextView, thirdName: TextView, thirdPoints: TextView) {
        val query: ParseQuery<Player> = ParseQuery.getQuery(Player::class.java)
        query.include(Player.KEY_USER)
        query.orderByDescending("totalPoints")
        query.findInBackground(object: FindCallback<Player>{
            override fun done(players: MutableList<Player>?, e: ParseException?) {
                if(e != null){
                    Log.e("LeaderboarFragment:","Error fetching users"+e)
                }else{
                    if (players != null){
                        if(players.size>0) {
                            Glide.with(firstPicture.context)
                                .load(players.get(0).getPImage()?.url).circleCrop()
                                .into(firstPicture)
                            firstName.text = players.get(0).getUser()?.username as String
                            firstPoints.text = players.get(0).getTotal().toString() + " Pts"
                        }
                        if(players.size>1){
                            Glide.with(firstPicture.context).load(players.get(1).getPImage()?.url).circleCrop().into(secondPicture)
                            secondName.text = players.get(1).getUser()?.username as String
                            secondPoints.text = players.get(1).getTotal().toString()+" Pts"
                        }
                        if(players.size>2){
                            Glide.with(firstPicture.context).load(players.get(2).getPImage()?.url).circleCrop().into(thirdPicture)
                            thirdName.text = players.get(2).getUser()?.username as String
                            thirdPoints.text = players.get(2).getTotal().toString()+" Pts"
                        }
                        if(players.size>3) {
                            allPlayers.addAll(players.slice(3 .. players.size-1 ))
                            adapter.notifyDataSetChanged()
                        }
                    }else{
                        Log.i("LeaderboardFragment:","Players is null")
                    }
                }
            }

        })
    }

    companion object {
        const val contextual = "hello"
    }
}