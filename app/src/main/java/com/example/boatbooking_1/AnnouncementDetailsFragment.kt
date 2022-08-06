package com.example.boatbooking_1

import android.app.ProgressDialog
import android.app.Service
import android.media.Image
import android.net.Uri
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
import com.example.boatbooking_1.viewmodel.DetailsAnnouncementViewModel

class AnnouncementDetailsFragment : Fragment() {

    private lateinit var binding: FragmentAnnouncementDetailsBinding
    private val announcementViewModel: AnnouncementViewModel by activityViewModels()
    private val detailsAnnouncementViewModel: DetailsAnnouncementViewModel by activityViewModels()

    private lateinit var observer: Observer<Announcement>

    private lateinit var AID: String

    private lateinit var servicesAdapter: PublicServiceAdapter
    private lateinit var serviceList: ArrayList<BoatService>
    private lateinit var rvService: RecyclerView

    private lateinit var imageAdapter: PublicImageAdapter
    private lateinit var remoteImageURIList: ArrayList<String>
    private lateinit var rvImages: RecyclerView

    private lateinit var imagesName: ArrayList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        serviceList = ArrayList()
        servicesAdapter = PublicServiceAdapter(serviceList)

        remoteImageURIList = ArrayList()
        imagesName = ArrayList()
        imageAdapter = PublicImageAdapter(this.context!!, remoteImageURIList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        remoteImageURIList.clear()
        imagesName.clear()

        // Inflate the layout for this fragment
        binding = FragmentAnnouncementDetailsBinding.inflate(inflater, container, false)

        announcementViewModel.setAnnouncement(arguments!!.getString("id"), requireContext())

        rvService = binding.rvServices
        rvService.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        rvService.adapter = servicesAdapter

        rvImages = binding.rvImages
        rvImages.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        rvImages.adapter = imageAdapter

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
            binding.tvDescription.setText(announcement.description.toString())

            serviceList.clear()
            serviceList.addAll(announcement.services!!)
            servicesAdapter.notifyDataSetChanged()

            imagesName.clear()
            remoteImageURIList.clear()
            getImageForAnnouncement(announcement.imageList!!, imageAdapter,remoteImageURIList)
        }

        announcementViewModel.getAnnouncement().observe(viewLifecycleOwner, observer)



        //getImageForAnnouncement(arguments!!.getString("id")!!, imageList, imageAdapter)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            val action =
                AnnouncementDetailsFragmentDirections.actionAnnouncementDetailsFragmentToMainHome()
            findNavController().navigate(action)
        }

        /*binding.ivMainImg.setOnClickListener{
            Toast.makeText(context, "Clicked Main IMG", Toast.LENGTH_SHORT).show()
        }

         */
    }

    private fun getImageForAnnouncement(imagesName: ArrayList<String>, adapter: PublicImageAdapter,remoteImageURIList : ArrayList<String>,) {
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Caricamento immagini...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        detailsAnnouncementViewModel.getImageForAnnouncement(
            imagesName,
            adapter,
            progressDialog,
            remoteImageURIList
        )
    }
}