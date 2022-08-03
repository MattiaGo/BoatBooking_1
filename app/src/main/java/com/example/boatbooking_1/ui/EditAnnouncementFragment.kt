package com.example.boatbooking_1.ui

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boatbooking_1.databinding.FragmentEditAnnouncementBinding
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.model.Boat
import com.example.boatbooking_1.model.BoatService
import com.example.boatbooking_1.utils.Util
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

    private val announcementViewModel: AnnouncementViewModel by activityViewModels()

    private lateinit var binding: FragmentEditAnnouncementBinding
    private lateinit var observer: Observer<Announcement>

    private lateinit var serviceAdapter: ServiceAdapter
    private lateinit var serviceList: ArrayList<BoatService>
    private lateinit var imageAdapter: ImageAdapter

    private lateinit var newImageList: ArrayList<String>
    private lateinit var imageList: ArrayList<String>
    private var remoteImageURIList: ArrayList<String> = ArrayList()
    private var remoteImageList: ArrayList<String> = ArrayList()

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        serviceList = ArrayList()
        serviceAdapter = ServiceAdapter(serviceList)

        imageList = remoteImageURIList
        newImageList = ArrayList()
        imageAdapter = ImageAdapter(requireContext(), imageList, newImageList, remoteImageList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        Log.d("Context", childFragmentManager.toString())

        // Inflate the layout for this fragment
        binding = FragmentEditAnnouncementBinding.inflate(inflater, container, false)
//        return inflater.inflate(R.layout.fragment_edit_announcement, container, false)

        binding.backBtn.setOnClickListener {
            val action =
                EditAnnouncementFragmentDirections.actionEditAnnouncementFragmentToMyAnnouncementsFragment()
            findNavController().navigate(action)
        }

        Log.d("bundle", arguments!!.getString("id").toString())

        announcementViewModel.setAnnouncement(arguments!!.getString("id"), requireContext())

//        Log.d("announcementViewModel", announcementViewModel.announcement().toString())

        observer = Observer<Announcement> { announcement ->
            binding.titleBoat.text = announcement.boat?.name.toString()
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
            binding.etDescription.setText(announcement.description.toString())
//            imageList = announcement.imageList!!
//            Log.d("Adapter", imageList.toString())
//            imageAdapter.notifyDataSetChanged()


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

        announcementViewModel.setAnnouncement(arguments!!.getString("id"), requireContext())

        binding.backBtn.setOnClickListener {
            val action =
                EditAnnouncementFragmentDirections.actionEditAnnouncementFragmentToMyAnnouncementsFragment()
            findNavController().navigate(action)
            announcementViewModel.reset()
        }

        Log.d("bundle", arguments!!.getString("id").toString())

        binding.rvServices.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvServices.adapter = serviceAdapter

        binding.rvImages.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvImages.adapter = imageAdapter

//        serviceList = announcementViewModel.getAnnouncement().value!!.services!!

//        Log.d("announcementViewModel", announcementViewModel.announcement().toString())

        binding.btnAddService.setOnClickListener {
            serviceList.add(BoatService("Nuovo servizio", "0"))
            serviceAdapter.notifyDataSetChanged()
        }

        binding.btnAddImage.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                startActivityForResult(it, 0)
            }
        }

//        while (announcementViewModel.getAnnouncement().value == null) {
//            serviceList = announcementViewModel.getAnnouncement().value!!.services!!
//        }

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
                        serviceAdapter.notifyDataSetChanged()
                    }

                }

            }

        announcementViewModel.getAnnouncement().observe(viewLifecycleOwner, observer)

        getRemoteImages()

        binding.editBtn.setOnClickListener {

            // TODO: Update imageList
            updateImageList()

            val boat = Boat(
                name = binding.etBoatName.text.toString(),
                model = binding.etModel.text.toString(),
                builder = binding.etBuilder.text.toString(),
                year = binding.sliderYear.value.toInt(),
                length = binding.sliderLength.value.toInt(),
                passengers = binding.sliderMaxPassengers.value.toInt(),
                beds = binding.sliderBeds.value.toInt(),
                cabins = binding.sliderCabins.value.toInt(),
                bathrooms = binding.sliderBathroom.value.toInt()
            )

            val announcement = Announcement(
                id = announcementViewModel.getAnnouncement().value!!.id,
                id_owner = announcementViewModel.getAnnouncement().value!!.id_owner,
                announce_name = binding.etBoatName.text.toString(),
                boat = boat,
                capt_needed = binding.checkBoxCaptainNeeded.isChecked,
                licence_needed = binding.checkBoxLicense.isChecked,
                location = binding.etPort.text.toString(),
                description = binding.etDescription.text.toString(),
                services = serviceList,
                imageList = newImageList,
                available = announcementViewModel.getAnnouncement().value!!.available
            )

            announcementViewModel.refreshAnnouncement(announcement)
            announcementViewModel.updateAnnouncement(announcement.id!!)

            Toast.makeText(context, "Annuncio aggiornato correttamente!", Toast.LENGTH_SHORT).show()

            val action =
                EditAnnouncementFragmentDirections.actionEditAnnouncementFragmentToMyAnnouncementsFragment()
            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun updateImageList() {
        newImageList =
            (imageAdapter.newImageList + imageAdapter.remoteImageList) as ArrayList<String>

        // Remove remote images from Firebase Storage
        imageAdapter.remoteImageToRemove.forEach {
            Util.fStorage.getReference("images/$it").delete()
                .addOnSuccessListener {
                    Log.d("Storage", "OnSuccess: Image removed!")
                }
        }


        Log.d("Adapter", newImageList.toString())
    }

    private fun getRemoteImages() {
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Caricamento immagini...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        Util.fDatabase.collection("BoatAnnouncement")
            .document(Util.getUID()!!)
            .collection("Announcement")
            .document(arguments?.getString("id")!!)
            .get()
            .addOnCompleteListener {
                val images = it.result.get("imageList")
                val imageListMap = images as ArrayList<String>

                when (images) {
                    null -> {}
                    else -> {
                        if (imageListMap.isEmpty()) {
                            if (progressDialog.isShowing) {
                                progressDialog.dismiss()
                            }
                        }
//                Log.d("Firestore", serviceListString.toString())
                        for (image in imageListMap) {
//                val localFile = File.createTempFile("temp-image$i", ".jpg")

                            var downloadUri: Uri
//                var remoteImages: ArrayList<String> = ArrayList()

                            Util.fStorage.reference.child("/images/$image")
                                .downloadUrl
                                .addOnCompleteListener {
                                    // Got the download URL
                                    if (it.isSuccessful) {
                                        downloadUri = it.result
                                        Log.d("Adapter", "$downloadUri")
//                                        val generatedFilePath = downloadUri.toString()
                                        remoteImageURIList.add(downloadUri.toString())
                                        remoteImageList.add(image) // Name of image on Storage
                                        imageAdapter.notifyDataSetChanged()
                                        Log.d("Adapter", "$remoteImageURIList")
                                        /// The string (file link) that you need
                                        if (progressDialog.isShowing) {
                                            progressDialog.dismiss()
                                        }
                                    }

                                    if (progressDialog.isShowing) {
                                        progressDialog.dismiss()
                                    }
                                }
                                .addOnFailureListener {
                                    if (progressDialog.isShowing) {
                                        progressDialog.dismiss()
                                    }
                                    Log.d("Adapter", "Error: $it")
                                }
                        }
                    }
                }
            }
            .addOnFailureListener {
                if (progressDialog.isShowing) {
                    progressDialog.dismiss()
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 0) {
            val uri = data?.data

            Log.d("Adapter", "URI Image selected: ${uri.toString()}")
            imageList.add(uri.toString())
            newImageList.add(uri.toString())

            Log.d("Adapter", "Current imageList: $imageList")
            imageAdapter.notifyDataSetChanged()
        }
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