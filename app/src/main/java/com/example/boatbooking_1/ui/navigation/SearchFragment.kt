package com.example.boatbooking_1.ui.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.boatbooking_1.R

class SearchFragment : Fragment() {override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
): View? {
    val view = inflater.inflate(R.layout.fragment_search, container, false)

    view.findViewById<Button>(R.id.about_btn).setOnClickListener {
        findNavController().navigate(R.id.action_main_search_to_resultsFragment)
    }
    return view
}
}