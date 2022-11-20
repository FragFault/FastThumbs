package com.example.simpletodo.fragments

import CarouselRVAdapter
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.simpletodo.PlayDailyActivity
import com.example.simpletodo.R


class SelectionFragment : Fragment() {
    // initiate a Switch

    // TODO: Rename and change types of parameters
    var isCometetive: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_selection, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getActivity()?.getActionBar()?.hide();

        val ivDaily = view.findViewById<ImageView>(R.id.daily)
        val viewPager = view.findViewById<ViewPager2>(R.id.view_pager)

        Glide.with(view.context).load(R.drawable.poerty).into(ivDaily)

        ivDaily.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, PlayDailyActivity::class.java)

            context?.startActivity(intent)
        })


        viewPager.apply {
            clipChildren = false  // No clipping the left and right items
            clipToPadding = false  // Show the viewpager in full width without clipping the padding
            offscreenPageLimit = 3  // Render the left and right items
            (getChildAt(0) as RecyclerView).overScrollMode =
                RecyclerView.OVER_SCROLL_NEVER // Remove the scroll effect
        }

        //Set onclick
        view.findViewById<Switch>(R.id.modeSwitch).setOnClickListener {
            isCometetive = !isCometetive
            Toast.makeText(requireContext(), "Switced Modes $isCometetive", Toast.LENGTH_SHORT).show()
        }

//        val demoData = arrayListOf(
//            "Poetry",
//            "Movies",
//            "Lyrics"
//        )
        var demoData = mapOf(
            "Poetry" to context?.let { ResourcesCompat.getDrawable(it.resources, R.drawable.poerty, null) },
            "Movies" to context?.let { ResourcesCompat.getDrawable(it.resources, R.drawable.movie_clap, null) },
            "Lyrics" to context?.let { ResourcesCompat.getDrawable(it.resources, R.drawable.lyricspng, null) },
            "work" to context?.let { ResourcesCompat.getDrawable(it.resources, R.drawable.logo, null) }

            )

        viewPager.adapter = CarouselRVAdapter(demoData as Map<String, Drawable>)
//        viewPager.adapter = CarouselRVAdapter(demoData as Map<String, Drawable>)
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer((40 * Resources.getSystem().displayMetrics.density).toInt()))
//        compositePageTransformer.addTransformer { page, position ->
//            val r = 1 - kotlin.math.abs(position)
//            page.scaleY = (0.80f + r * 0.20f)
//        }
        viewPager.setPageTransformer(compositePageTransformer)


    }


    fun onClick(v: View?) {
        //Get notified of movie being tapped on
        //2. Use the intent system to navigate to the new activity
        val intent = Intent(context, PlayDailyActivity::class.java)

//            val options = makeSceneTransitionAnimation(
//                context,
//                (tvTitle as View?)!!, "profile"
//            )
//        intent.putExtra("MOVIE_EXTRA", movie)

        context?.startActivity(intent)

    }


}