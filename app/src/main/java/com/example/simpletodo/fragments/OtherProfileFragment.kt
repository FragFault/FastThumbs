package com.example.simpletodo.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.simpletodo.R


/**
 * A simple [Fragment] subclass.
 * Use the [OtherProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OtherProfileFragment : Fragment() {

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
        val message = bundle!!.getString("userId")

        if (message != null) {
            Log.i("Other", message)
        }

        val theMessage = view?.findViewById<TextView>(R.id.username)

        if (theMessage != null) {
            theMessage.text = message as String
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_other_profile, container, false)
    }

}