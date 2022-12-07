package com.example.simpletodo

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.simpletodo.fragments.OtherProfileFragment

class BoardAdapter(val context: Context, val players: List<Player>) : RecyclerView.Adapter<BoardAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_leaderboard, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BoardAdapter.ViewHolder, position: Int) {
        val player = players.get(position)
        holder.bind(player,position+4)
    }

    override fun getItemCount(): Int {
        return players.size
    }

   inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val rank : TextView
        val boardUsername:  TextView
        val points: TextView
        val image: ImageView
        init{
            rank = itemView.findViewById(R.id.boardRank)
            boardUsername = itemView.findViewById(R.id.boardUsername)
            points = itemView.findViewById(R.id.boardPoints)
            image = itemView.findViewById(R.id.boardPicture)
            itemView.setOnClickListener(this)
        }

        fun bind(player: Player,index: Int){
            rank.text = index.toString()
            boardUsername.text = player.getUser()?.username as String
            points.text = player.getTotal().toString()+" Pts"
            Glide.with(itemView.context).load(player.getPImage()?.url).circleCrop().into(image)
        }

       override fun onClick(v: View?) {
           val thePlayer = players[adapterPosition]
           val userId = thePlayer.getUser().toString()

           Log.i(TAG, "The thing was clicked: $userId")

           val bundle = Bundle()
           bundle.putString("userId", userId)
            // set Fragmentclass Arguments
           val fragObj = OtherProfileFragment()
           fragObj.setArguments(bundle)

           (context as FragmentActivity).supportFragmentManager.beginTransaction()
               .replace(R.id.flContainer, fragObj)
               .commit()
       }
   }

    companion object {
        private const val TAG = "ADAPTER"

    }
}