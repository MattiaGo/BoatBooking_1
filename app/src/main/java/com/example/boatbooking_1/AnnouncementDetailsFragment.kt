package com.example.boatbooking_1

import android.app.Service
import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.boatbooking_1.databinding.FragmentAnnouncementDetailsBinding
import com.example.boatbooking_1.databinding.FragmentMyAnnouncementsBinding
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.model.Boat
import com.example.boatbooking_1.model.BoatService
import com.example.boatbooking_1.ui.*
import com.example.boatbooking_1.utils.Util
import com.example.boatbooking_1.viewmodel.AnnouncementViewModel

class AnnouncementDetailsFragment : Fragment() {

    private lateinit var binding: FragmentAnnouncementDetailsBinding
    private val announcementViewModel: AnnouncementViewModel by activityViewModels()
    private lateinit var observer: Observer<Announcement>

    private lateinit var servicesAdapter: PublicServiceAdapter
    private lateinit var serviceList: ArrayList<BoatService>
    private lateinit var rvService: RecyclerView

    private lateinit var imageAdapter: ImageAdapter
    private lateinit var imageList: ArrayList<String>
    private lateinit var rvImages: RecyclerView
    private lateinit var newImageList: ArrayList<String>


    private var remoteImageURIList: ArrayList<String> = ArrayList()
    private var remoteImageList: ArrayList<String> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        serviceList = ArrayList()
        servicesAdapter = PublicServiceAdapter(serviceList)

        imageList = remoteImageURIList
        newImageList = ArrayList()
        imageAdapter = ImageAdapter(requireContext(), imageList, newImageList, remoteImageList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        serviceList.clear()

        // Inflate the layout for this fragment
        binding = FragmentAnnouncementDetailsBinding.inflate(inflater, container, false)

        announcementViewModel.setAnnouncement(arguments!!.getString("id"), requireContext())

//        Log.d("announcementViewModel", announcementViewModel.announcement().toString())

        observer = Observer<Announcement> { announcement ->
            binding.boatName.setText(announcement.boat?.name.toString())
            binding.tvModel.setText(announcement.boat?.model.toString())
            binding.tvBuilder.setText(announcement.boat?.builder.toString())
            binding.tvYear.setText(announcement.boat?.year!!.toString())
            binding.tvLength.setText(announcement.boat?.length!!.toString())
            binding.tvPassengers.setText(announcement.boat?.passengers!!.toString())
            binding.tvBeds.setText(announcement.boat?.beds!!.toString())
            binding.tvCabins.setText(announcement.boat?.cabins!!.toString())
            binding.tvBathrooms.setText(announcement.boat?.bathrooms!!.toString())

            binding.tvLocation.setText(announcement.location.toString())

            binding.layoutSkipper.visibility = 1
            if(!announcement.licence_needed!!) {
                binding.tvSkipperCond.setText("Non è richiesta la patente nautica")
            }
            if(announcement.capt_needed!!){
                binding.layoutCaptain.isVisible = true
            }

            //TODO: immagini e servizi

            rvService = binding.rvServices
            rvService.layoutManager =
                LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            rvService.adapter = servicesAdapter

            rvImages = binding.rvImages
            rvImages.layoutManager =
                LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            rvImages.adapter = imageAdapter

            binding.tvDescription.setText(announcement.description.toString())


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

        Util.fDatabase.collection("BoatAnnouncement")
            .document(Util.getUID()!!)
            .collection("Announcement")
            .document(arguments?.getString("id")!!)
            .get()
            .addOnSuccessListener {
                val services = it.get("services")
                Log.d("Firestore", it.toString())
                val serviceListMap = services as ArrayList<HashMap<String, String>>

//                Log.d("Firestore", serviceListString.toString())
                when (services) {
                    null -> {}
                    else -> {
                        for (service in serviceListMap) {
                            val boatService = BoatService(
                                service["name"]!!,
                                service["price"]!!
                            )

                            serviceList.add(boatService)
//                    Log.d("Firestore", service["name"]!!)
//                    Log.d("Firestore", service["price"]!!)
                        }
                        servicesAdapter.notifyDataSetChanged()
                    }

                }

            }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            val action =
                AnnouncementDetailsFragmentDirections.actionAnnouncementDetailsFragmentToMainHome()
            findNavController().navigate(action)
        }

        binding.ivMainImg.setOnClickListener{
            Toast.makeText(context, "Clicked Main IMG", Toast.LENGTH_SHORT).show()
        }

    }
}