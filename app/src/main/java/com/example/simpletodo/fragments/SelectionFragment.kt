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
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.simpletodo.PlayActivity
import com.example.simpletodo.R
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.simpletodo.Player
//import com.codepath.asynchttpclient.AsyncHttpClient
//import com.codepath.asynchttpclient.RequestParams
//import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
//import com.codepath.asynchttpclient.callback.TextHttpResponseHandler
import com.parse.ParseQuery
import com.parse.ParseUser
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit


class SelectionFragment : Fragment() {
    // initiate a Switch
    private val LYRICS_URL = "http://52.87.203.225/api/getLyricsPrompt"
    private val POETRY_URL = "https://52/.87/.203/.225/api/getPoetryPrompt"
    private val MOVIE_URL =  "http://52.87.203.225/api/getMoviePrompt"
    lateinit var profilePic : ImageView
    lateinit var user : ParseUser

    // TODO: Rename and change types of parameters

    companion object {
        const val TAG = "SelectionScreenMessages"
        @JvmStatic var isCompetetive: Boolean = false
    }

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
        profilePic = view.findViewById<ImageView>(R.id.ivProfileImage)

        var prompt_Data = LYRICS_URL

        Glide.with(view.context).load(R.drawable.question_mark_2).into(ivDaily) //Load pic into daily challenge

        val player = ParseUser.getCurrentUser().username as String
        user = ParseUser.getCurrentUser()
        profiledetails()

        val titleString = "Let's Type"

        val rnds = (0..2).random()
        Log.i("RANDOM", rnds.toString())

        if(rnds == 0){
            prompt_Data = LYRICS_URL
        }else if(rnds == 1){
            prompt_Data = POETRY_URL
        }else{
            prompt_Data = MOVIE_URL
        }

        view.findViewById<Switch>(R.id.modeSwitch).setOnClickListener {
            isCompetetive = !isCompetetive
//            Toast.makeText(requireContext(), "Switced Modes $isCompetetive", Toast.LENGTH_SHORT).show()

        }

        titleView.text = titleString + " " + player + "!"
        ivDaily.setOnClickListener(View.OnClickListener {

            dataRetrieve(prompt_Data, "Movies")
            if (it != null) {
                it.setOnClickListener(null)
            }

        })

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

        viewPager.adapter = requireContext()?.let {
//            isCompetetive = !isCompetetive

            Log.i(TAG, isCompetetive.toString())//Pass Context as a parameter for switching intents
            CarouselRVAdapter(demoData as Map<String, Drawable>,
                it, isCompetetive
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

                    Log.i(TAG, getContext().toString())
                    if (element.getPImage() != null) {
                        activity?.let {
                            Glide.with(it)
                                .load(element.getPImage()?.url)
                                .transform(CircleCrop())
                                .into(profilePic)
                        }
                    } else {
                        activity?.let {
                            Glide.with(it)
                                .load(R.drawable.instagram_user_filled_24)
                                .transform(CircleCrop())
                                .into(profilePic)
                        }
                    }

                }
            } else {
                Log.e(TAG, "Parse Error: ", e)
            }
        }
        Log.e(TAG, "This works")

    }

    fun dataRetrieve(CATEGORY_URL: String, selection: String) {

        val client = OkHttpClient.Builder().connectTimeout(100, TimeUnit.SECONDS).writeTimeout(100,
            TimeUnit.SECONDS).readTimeout(300, TimeUnit.SECONDS).build()

        Log.e("dataRetrieve",CATEGORY_URL)
        val request = okhttp3.Request.Builder().url(CATEGORY_URL).build()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("dataRetrieve:","FAIL")
            }
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                if(body!=null){
                    val jObject = JSONObject(body)
                    val intent = Intent(context,PlayActivity::class.java)
                    intent.putExtra("prompt",jObject.get("prompt").toString())
                    context?.startActivity(intent)

                }
            }
        })

    }


}

