package com.example.simpletodo.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simpletodo.BoardAdapter
import com.example.simpletodo.Player
import com.example.simpletodo.R
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery

/**
 * A simple [Fragment] subclass.
 * Use the [LeaderboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LeaderboardFragment : Fragment() {
    lateinit var boardRecyclerView: RecyclerView
    lateinit var adapter: BoardAdapter

    var allPlayers: MutableList<Player> = mutableListOf()
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
        queryBoard()
    }

    fun queryBoard(){
        val query: ParseQuery<Player> = ParseQuery.getQuery(Player::class.java)
        query.include(Player.KEY_USER)
        query.findInBackground(object: FindCallback<Player>{
            override fun done(players: MutableList<Player>?, e: ParseException?) {
                    if(e != null){
                        Log.e("LeaderboarFragment:","Error fetching users")
                    }else{
                        if (players != null){
                            Log.e("yo:",players.size.toString())
                            for (player in players){
                                Log.i("LeaderboardFragment:","Player:"+player.getBio()!!)
                            }

                            allPlayers.addAll(players)
                            adapter.notifyDataSetChanged()
                        }else{
                            Log.i("LeaderboardFragment:","Players is null")
                        }
                    }
            }

        })
    }
}