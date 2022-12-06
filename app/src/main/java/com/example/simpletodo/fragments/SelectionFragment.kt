package com.example.simpletodo.fragments

import CarouselRVAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
//import com.codepath.asynchttpclient.AsyncHttpClient
//import com.codepath.asynchttpclient.RequestParams
//import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
//import com.codepath.asynchttpclient.callback.TextHttpResponseHandler
import com.example.simpletodo.R
import com.example.simpletodo.ResultActivity
import com.parse.ParseUser
import org.json.JSONException


class SelectionFragment : Fragment() {
    // initiate a Switch
    private val LYRICS_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"
    private val POETRY_URL = "https://52/.87/.203/.225/api/getPoetryPrompt"
    private val MOVIE_URL =  "http://52.87.203.225/api/getMoviePrompt"

    // TODO: Rename and change types of parameters
    var isCometetive: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_selection, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getActivity()?.getActionBar()?.hide();

        val ivDaily = view.findViewById<ImageView>(R.id.daily)
        val viewPager = view.findViewById<ViewPager2>(R.id.view_pager)
        val titleView = view.findViewById<TextView>(R.id.Text)
//        val ivPic = view.findViewById<ImageView>(R.id.ivProfileImage)

        Glide.with(view.context).load(R.drawable.poerty).into(ivDaily) //Load pic into daily challenge

        val player = ParseUser.getCurrentUser().username as String

        val titleString = "Let's Type"

        titleView.text = titleString + " " + player + "!"
        ivDaily.setOnClickListener(View.OnClickListener {

            val intent = Intent(context, ResultActivity::class.java)

            context?.startActivity(intent)
        })

        //Set onClick  Switch
        view.findViewById<Switch>(R.id.modeSwitch).setOnClickListener {
            isCometetive = !isCometetive
            Toast.makeText(requireContext(), "Switced Modes $isCometetive", Toast.LENGTH_SHORT).show()
        }


        viewPager.apply {
            clipChildren = false  // No clipping the left and right items
            clipToPadding = false  // Show the viewpager in full width without clipping the padding
            offscreenPageLimit = 3  // Render the left and right items
            (getChildAt(0) as RecyclerView).overScrollMode =
                RecyclerView.OVER_SCROLL_NEVER // Remove the scroll effect
        }

        val demoData = mapOf(
            "Poetry" to context?.let { ResourcesCompat.getDrawable(it.resources, R.drawable.poerty, null) },
            "Movies" to context?.let { ResourcesCompat.getDrawable(it.resources, R.drawable.movie_clap, null) },
            "Lyrics" to context?.let { ResourcesCompat.getDrawable(it.resources, R.drawable.lyricspng, null) },

            )

        viewPager.adapter = context?.let { //Pass Context as a parameter for switching intents
            CarouselRVAdapter(demoData as Map<String, Drawable>,
                it
            )
        }

        //Makes the Carousel slide and sets animations
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer((40 * Resources.getSystem().displayMetrics.density).toInt()))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - kotlin.math.abs(position)
            page.scaleY = (0.80f + r * 0.20f)
        }
        viewPager.setPageTransformer(compositePageTransformer)

    }




    companion object {
        const val TAG = "SelectionScreenMessages"
    }



}

