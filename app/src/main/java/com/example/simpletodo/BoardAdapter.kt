package com.example.simpletodo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.parse.ParseUser

class BoardAdapter(val context: Context, val players: List<Player>) : RecyclerView.Adapter<BoardAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_leaderboard, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BoardAdapter.ViewHolder, position: Int) {
        val player = players.get(position)
        holder.bind(player,position+3)
    }

    override fun getItemCount(): Int {
        return players.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val rank : TextView
        val boardUsername:  TextView
        val points: TextView
        val image: ImageView

        init{
            rank = itemView.findViewById(R.id.boardRank)
            boardUsername = itemView.findViewById(R.id.boardUsername)
            points = itemView.findViewById(R.id.boardPoints)
            image = itemView.findViewById(R.id.boardPicture)
        }

        fun bind(player: Player,index: Int){
            rank.text = index.toString()
            boardUsername.text = player.getUser()?.username as String
            points.text = player.getTotal().toString()+" Pts"
            Glide.with(itemView.context).load(player.getPImage()?.url).into(image)
        }
    }
}