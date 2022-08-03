package com.example.boatbooking_1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.boatbooking_1.databinding.FragmentAnnouncementDetailsBinding
import com.example.boatbooking_1.databinding.FragmentMyAnnouncementsBinding
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.ui.MyAnnouncementsFragmentDirections
import com.example.boatbooking_1.viewmodel.AnnouncementViewModel

class AnnouncementDetailsFragment : Fragment() {

    private lateinit var binding: FragmentAnnouncementDetailsBinding
    private val announcementViewModel: AnnouncementViewModel by activityViewModels()
    private lateinit var observer: Observer<Announcement>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAnnouncementDetailsBinding.inflate(inflater, container, false)

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            val action =
                AnnouncementDetailsFragmentDirections.actionAnnouncementDetailsFragmentToMainHome()
            findNavController().navigate(action)
        }

    }
}