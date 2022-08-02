package com.example.boatbooking_1.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.size
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.boatbooking_1.R
import com.example.boatbooking_1.databinding.FragmentEditAnnouncementBinding
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.model.Boat
import com.example.boatbooking_1.viewmodel.AnnouncementViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditAnnouncementFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditAnnouncementFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentEditAnnouncementBinding
    private val announcementViewModel: AnnouncementViewModel by activityViewModels()
    private lateinit var observer: Observer<Announcement>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditAnnouncementBinding.inflate(inflater, container, false)
//        return inflater.inflate(R.layout.fragment_edit_announcement, container, false)

        binding.backBtn.setOnClickListener {
            val action = EditAnnouncementFragmentDirections.actionEditAnnouncementFragmentToMyAnnouncementsFragment()
            findNavController().navigate(action)
        }

        Log.d("bundle", arguments!!.getString("id").toString())

        announcementViewModel.setAnnouncement(arguments!!.getString("id"))

//        Log.d("announcementViewModel", announcementViewModel.announcement().toString())

        observer = Observer<Announcement> { announcement ->
            binding.titleBoat.setText(announcement.boat?.name.toString())
            binding.etBoatName.setText(announcement.boat?.name.toString())
            binding.etModel.setText(announcement.boat?.model.toString())
            binding.etBuilder.setText(announcement.boat?.builder.toString())
            binding.sliderYear.value = announcement.boat?.year!!.toFloat()
            binding.sliderLength.value = announcement.boat?.length!!.toFloat()
            binding.sliderMaxPassengers.value = announcement.boat?.passengers!!.toFloat()
            binding.sliderBeds.value = announcement.boat?.beds!!.toFloat()
            binding.sliderCabins.value = announcement.boat?.cabins!!.toFloat()
            binding.sliderBathroom.value = announcement.boat?.bathrooms!!.toFloat()

            binding.etPort.setText(announcement.location.toString())
            binding.checkBoxLicense.isChecked = announcement.licence_needed!!
            binding.checkBoxCaptainNeeded.isChecked = announcement.capt_needed!!

            //TODO: immagini e servizi



            binding.etDescription.setText(announcement.description.toString())


            /*val yearSpinner = binding.sliderYear
            val lengthSpinner = binding.sliderLength

            val yearsArray = resources.getStringArray(R.array.years_array)
            val lengthsArray = resources.getStringArray(R.array.lengths_array)

            yearsArray.forEachIndexed { index, s ->
                if (s.equals(announcement.boat?.year.toString()))
                    yearSpinner.setSelection(index)
            }

            lengthsArray.forEachIndexed { index, s ->
                if (s.equals(announcement.boat?.length.toString()))
                    lengthSpinner.setSelection(index)
            }*/
        }

        announcementViewModel.getAnnouncement().observe(viewLifecycleOwner, observer)

        binding.editBtn.setOnClickListener {
            val boat = Boat(
                name= binding.etBoatName.text.toString(),
                model = binding.etModel.text.toString(),
                builder = binding.etBuilder.text.toString(),
                year = binding.sliderYear.value.toInt(),
                length = binding.sliderLength.value.toInt(),
                passengers = binding.sliderMaxPassengers.value.toInt(),
                beds = binding.sliderBeds.value.toInt(),
                cabins =  binding.sliderCabins.value.toInt(),
                bathrooms =  binding.sliderBathroom.value.toInt()
            )

            val announcement = Announcement(
                id= announcementViewModel.getAnnouncement().value!!.id,
                announce_name = binding.etBoatName.text.toString(),
                boat = boat,
                capt_needed = binding.checkBoxCaptainNeeded.isChecked,
                licence_needed = binding.checkBoxLicense.isChecked,
                location = binding.etPort.text.toString(),
                description = binding.etDescription.text.toString(),
                services = arrayListOf(),
                imageList = arrayListOf(),
                available = true
            )

            announcementViewModel.refreshAnnouncement(announcement)
            announcementViewModel.updateAnnouncement()

            Toast.makeText(context, "Annuncio aggiornato correttamente!", Toast.LENGTH_SHORT).show()

            val action = EditAnnouncementFragmentDirections.actionEditAnnouncementFragmentToMyAnnouncementsFragment()
            findNavController().navigate(action)
        }

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditAnnouncementFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditAnnouncementFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}