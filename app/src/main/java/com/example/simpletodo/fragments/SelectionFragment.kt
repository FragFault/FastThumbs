package com.example.simpletodo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.simpletodo.R


class SelectionFragment : Fragment() {
    // initiate a Switch

    // TODO: Rename and change types of parameters
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

        //Set onclick
        view.findViewById<Switch>(R.id.modeSwitch).setOnClickListener {
            Toast.makeText(requireContext(), "Seems like theres an issue submitting", Toast.LENGTH_SHORT).show()
        }

    }



}