package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.simpletodo.fragments.SelectionFragment

open class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager: FragmentManager = supportFragmentManager

        var fragmentToShow: Fragment? = null

        fragmentToShow = SelectionFragment()

        if(fragmentToShow != null){
            fragmentManager.beginTransaction().replace(R.id.flContainer, fragmentToShow).commit()
        }

    }
}