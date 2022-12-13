package com.example.simpletodo

import android.content.Context
import android.util.Log
import android.view.KeyCharacterMap.load
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import java.lang.System.load

class GameLogAdapter(val context: Context, val gameLogs: List<GameResults>) : RecyclerView.Adapter<GameLogAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameLogAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_game_details, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameLogAdapter.ViewHolder, position: Int) {
        val gameLog = gameLogs.get(position)
        holder.bind(gameLog)
    }

    override fun getItemCount(): Int {
        return gameLogs.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val date : TextView
        val speed:  TextView
        val accuracy: TextView
        val categoryIMG : ImageView


        init{
            date = itemView.findViewById(R.id.date6)
            speed = itemView.findViewById(R.id.speedstat)
            accuracy = itemView.findViewById(R.id.accstat)
            categoryIMG = itemView.findViewById(R.id.imageView11)
        }

        fun bind(gameLog: GameResults){

            val datestring = gameLog.createdAt.toString()
            val datearray  = datestring.split(" ")
            val length = datearray[1] + " " + datearray[2] + " " + datearray[5]
            date.text = length
            val category = gameLog.getCategory()

            if (category == "poetry") {
                Glide.with(itemView).load(R.drawable.book).into(categoryIMG)
            } else if (category == "movie"){
                Glide.with(itemView).load(R.drawable.camera).into(categoryIMG)

            } else {
                Glide.with(itemView).load(R.drawable.paper).into(categoryIMG)

            }
            speed.text = gameLog.getSpeed().toString()+" WPM / "
            accuracy.text = gameLog.getAccuracy().toString()+" %"
        }
    }
    companion object {
        const val KEY_PFP = "profilePic"
        private const val TAG = "ProfileFragment"
    }
}