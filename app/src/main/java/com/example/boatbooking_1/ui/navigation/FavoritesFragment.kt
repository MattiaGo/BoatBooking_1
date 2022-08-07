package com.example.boatbooking_1.ui.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.boatbooking_1.R
import com.example.boatbooking_1.databinding.FragmentFavoritesBinding
import com.example.boatbooking_1.databinding.FragmentHomeBinding
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.ui.FavoritesAnnouncementAdapter
import com.example.boatbooking_1.ui.PublicAnnouncementAdapter
import com.example.boatbooking_1.viewmodel.FavoritesBoatsViewModel
import com.example.boatbooking_1.viewmodel.HomeAnnouncementViewModel

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding

    private val favoritesBoatsViewModel: FavoritesBoatsViewModel by activityViewModels()

    private lateinit var tvNoFavorites: TextView
    private lateinit var favoritesAdapter: FavoritesAnnouncementAdapter
    private lateinit var favoritesList: ArrayList<Announcement>
    private lateinit var rvFavorites: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        favoritesList = ArrayList()
        favoritesAdapter = FavoritesAnnouncementAdapter(favoritesList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        favoritesList.clear()
        // Inflate the layout for this fragment
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        rvFavorites = binding.rvFavorites
        rvFavorites.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        rvFavorites.adapter = favoritesAdapter

        favoritesBoatsViewModel.getFavoritesBoats(
            favoritesList,
            favoritesAdapter
        )
        return binding.root

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Preferiti.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavoritesFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}