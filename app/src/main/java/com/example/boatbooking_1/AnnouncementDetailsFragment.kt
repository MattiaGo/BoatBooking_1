package com.example.boatbooking_1

import com.example.boatbooking_1.R
import android.app.ProgressDialog
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.compose.ui.graphics.Color
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.boatbooking_1.databinding.FragmentAnnouncementDetailsBinding
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.model.BoatService
import com.example.boatbooking_1.ui.*
import com.example.boatbooking_1.utils.Util
import com.example.boatbooking_1.viewmodel.AnnouncementViewModel
import com.example.boatbooking_1.viewmodel.DetailsAnnouncementViewModel
import com.example.boatbooking_1.viewmodel.FavoritesBoatsViewModel
import com.example.boatbooking_1.viewmodel.UserProfileViewModel
import com.google.android.material.snackbar.Snackbar

class AnnouncementDetailsFragment : Fragment() {

    private lateinit var binding: FragmentAnnouncementDetailsBinding
    private val announcementViewModel: AnnouncementViewModel by activityViewModels()
    private val detailsAnnouncementViewModel: DetailsAnnouncementViewModel by activityViewModels()
    private val favoritesBoatsViewModel: FavoritesBoatsViewModel by activityViewModels()
    private val userViewModel: UserProfileViewModel by activityViewModels()

    private lateinit var observer: Observer<Announcement>

    private lateinit var AID: String

    private lateinit var servicesAdapter: PublicServiceAdapter
    private lateinit var serviceList: ArrayList<BoatService>
    private lateinit var rvService: RecyclerView

    private lateinit var imageAdapter: PublicImageAdapter
    private lateinit var rvImages: RecyclerView

    private lateinit var imagesName: ArrayList<String>

    private var remoteImageURIList: ArrayList<String> = ArrayList()


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

        //if (announcementViewModel.getAnnouncement().value == null) {
        if (!arguments!!.getString("id").isNullOrBlank())
            announcementViewModel.setAnnouncement(
                arguments!!.getString("id"),
                requireContext()
            )
//        } else {
//            announcementViewModel.setAnnouncement(arguments!!.getString("id"), requireContext())
//        }

        rvService = binding.rvServices
        rvService.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        rvService.adapter = servicesAdapter

        rvImages = binding.rvImages
        rvImages.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        rvImages.adapter = imageAdapter

        // RecyclerView
        rvService = binding.rvServices
        rvService.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        rvService.adapter = servicesAdapter

        // RecyclerView
        rvImages = binding.rvImages
        rvImages.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        rvImages.adapter = imageAdapter

        observer = Observer<Announcement> { announcement ->
            binding.boatName.text = announcement.boat?.name.toString()
            binding.tvModel.text = announcement.boat?.model.toString()
            binding.tvBuilder.text = announcement.boat?.builder.toString()
            binding.tvYear.text = announcement.boat?.year!!.toString()
            binding.tvLength.text = announcement.boat?.length!!.toString()
            binding.tvPassengers.text = announcement.boat?.passengers!!.toString()
            binding.tvBeds.text = announcement.boat?.beds!!.toString()
            binding.tvCabins.text = announcement.boat?.cabins!!.toString()
            binding.tvBathrooms.text = announcement.boat?.bathrooms!!.toString()
            binding.tvLocation.text = announcement.location.toString()
            binding.tvDescription.text = announcement.description.toString()
            binding.tvPrice.text = announcement.price.toString()

            binding.layoutSkipper.visibility = 1
            if (!announcement.licence_needed!!) {
                binding.tvSkipperCond.text = "Non è richiesta la patente nautica"
            }
            if (announcement.capt_needed!!) {
                binding.layoutCaptain.isVisible = true
            }

            binding.tvDescription.setText(announcement.description.toString())

            serviceList.clear()
            serviceList.addAll(announcement.services!!)
            servicesAdapter.notifyDataSetChanged()

            imagesName.clear()
            remoteImageURIList.clear()
            getImageForAnnouncement(announcement.imageList!!, imageAdapter, remoteImageURIList)

            if (!Util.getUID().isNullOrBlank()) {
                detailsAnnouncementViewModel.checkIfFavorite(announcement.id!!, binding.likeBtn)
            }
        }

        announcementViewModel.getAnnouncement().observe(viewLifecycleOwner, observer)

        rvService.refreshDrawableState()
        servicesAdapter.notifyDataSetChanged()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (userViewModel.getUser().value == null) {
            binding.bookBtn.setOnClickListener {
                Snackbar.make(
                    activity!!.findViewById(android.R.id.content),
                    getString(R.string.booking_validation),
                    Snackbar.LENGTH_SHORT
                )
                    .setActionTextColor(Color.White.hashCode())
                    .setAnchorView(R.id.bottom_nav)
                    .setAction("HO CAPITO") { // Responds to click on the action
                    }.show()
            }

            binding.btnSendMessage.setOnClickListener {
                Snackbar.make(
                    activity!!.findViewById(android.R.id.content),
                    getString(R.string.send_message_validation),
                    Snackbar.LENGTH_SHORT
                )
                    .setActionTextColor(Color.White.hashCode())
                    .setAnchorView(R.id.bottom_nav)
                    .setAction("HO CAPITO") { // Responds to click on the action
                    }.show()
            }
        }

        if (userViewModel.getUser().value?.shipOwner == true) {
            binding.bookBtn.setOnClickListener {
                Snackbar.make(
                    activity!!.findViewById(android.R.id.content),
                    getString(R.string.owner_booking_validation),
                    Snackbar.LENGTH_SHORT
                )
                    .setActionTextColor(Color.White.hashCode())
                    .setAnchorView(R.id.bottom_nav)
                    .setAction("HO CAPITO") { // Responds to click on the action
                    }.show()
            }

            binding.btnSendMessage.setOnClickListener {
                Snackbar.make(
                    activity!!.findViewById(android.R.id.content),
                    getString(R.string.send_message_validation),
                    Snackbar.LENGTH_SHORT
                )
                    .setActionTextColor(Color.White.hashCode())
                    .setAnchorView(R.id.bottom_nav)
                    .setAction("HO CAPITO") { // Responds to click on the action
                    }.show()

            }

        }

//        Log.d("MyDebug", userViewModel.getUser().value?.shipOwner!!.toString())
        if (!Util.getUID().isNullOrBlank() && userViewModel.getUser().value?.shipOwner == false) {

            binding.btnSendMessage.setOnClickListener {

                val bundle = Bundle()
                bundle.putString("id_owner", announcementViewModel.getAnnouncement().value!!.id_owner)
                findNavController().navigate(R.id.chatFragmentFromAnnouncement, bundle)
//                Snackbar.make(
//                    activity!!.findViewById(android.R.id.content),
//                    "Invia messaggio",
//                    Snackbar.LENGTH_SHORT
//                )
//                    .setActionTextColor(Color.White.hashCode())
//                    .setAnchorView(R.id.bottom_nav)
//                    .setAction("HO CAPITO") { // Responds to click on the action
//                    }.show()
            }

            binding.bookBtn.setOnClickListener {
                val action =
                    AnnouncementDetailsFragmentDirections.actionAnnouncementDetailsFragmentToBookingFragment()
                findNavController().navigate(action)
            }
        }

        binding.backBtn.setOnClickListener {
            val action =
                AnnouncementDetailsFragmentDirections.actionAnnouncementDetailsFragmentToMainHome()
            findNavController().navigate(action)
        }

        binding.likeBtn.setOnClickListener {
            if (!Util.getUID().isNullOrBlank()) {
                val announcement = announcementViewModel.getAnnouncement().value
                favoritesBoatsViewModel.manageFavoritesBoat(
                    announcement = announcement!!,
                    binding.likeBtn
                )
            } else {
                Snackbar.make(
                    activity!!.findViewById(android.R.id.content),
                    "Effettua il login per salvare questa barca tra le tue preferite",
                    Snackbar.LENGTH_LONG
                ).setAnchorView(R.id.bottom_nav).setAction("HO CAPITO") {
                    // Responds to click on the action
                }.show()
//            Toast.makeText(context, "Effettua il login per visualizzare i tuoi messaggi", Toast.LENGTH_SHORT).show()

            }
        }
    }


//        binding.btnSendMessage.setOnClickListener {
//            if (!Util.getUID().isNullOrBlank()) {
//                //TODO: manda messaggio
//            } else {
//                Snackbar.make(
//                    activity!!.findViewById(android.R.id.content),
//                    "Effettua il login per contattare il proprietario", Snackbar.LENGTH_LONG
//                ).setAnchorView(com.example.boatbooking_1.R.id.bottom_nav).setAction("HO CAPITO") {
//                    // Responds to click on the action
//                }.show()
////            Toast.makeText(context, "Effettua il login per visualizzare i tuoi messaggi", Toast.LENGTH_SHORT).show()
//
//            }
//        }

    private fun getImageForAnnouncement(
        imagesName: ArrayList<String>,
        adapter: PublicImageAdapter,
        remoteImageURIList: ArrayList<String>
    ) {
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