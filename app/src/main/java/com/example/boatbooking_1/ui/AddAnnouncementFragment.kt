package com.example.boatbooking_1.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.boatbooking_1.R
import com.example.boatbooking_1.databinding.FragmentAddAnnouncementBinding
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.model.BoatService
import com.example.boatbooking_1.repository.AnnouncementRepository
import com.example.boatbooking_1.utils.Util
import com.example.boatbooking_1.viewmodel.AnnouncementViewModel
import com.example.boatbooking_1.viewmodel.BoatViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [AddAnnouncementFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddAnnouncementFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null

    /* TODO: Using the activityViewModels() Kotlin property delegate from the fragment-ktx artifact
        to retrieve the ViewModel in the activity scope
     */

    private lateinit var binding: FragmentAddAnnouncementBinding
    private lateinit var btnSave: Button
    private lateinit var etPort: TextInputEditText
    private lateinit var checkBoxLicense: CheckBox
    private lateinit var checkBoxCaptain: CheckBox
    private lateinit var etDescription: TextInputEditText
    private lateinit var rvImages: RecyclerView
    private lateinit var rvServices: RecyclerView
    private lateinit var btnAddImage: ImageButton
    private lateinit var btnAddService: ImageButton

    private lateinit var serviceAdapter: ServiceAdapter
    private lateinit var imageAdapter: ImageAdapter

    private lateinit var imageList: ArrayList<String>
    private lateinit var serviceList: ArrayList<BoatService>

    private lateinit var fDatabase: FirebaseFirestore

    private val boatViewModel: BoatViewModel by activityViewModels()
    private val announceViewModel: AnnouncementViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }

        fDatabase = FirebaseFirestore.getInstance()
        imageList = ArrayList()
        serviceList = ArrayList()

        imageAdapter = ImageAdapter(imageList)
        serviceAdapter = ServiceAdapter(serviceList)

//        imageList.add(0, "")

//        imageList.add(Util.path + "/DCIM/boat-test.jpeg")

//        val ref = Util.fStorage.child("images/boat-test.jpeg")
//        ref.downloadUrl.addOnSuccessListener {
//            val downUri: Uri = it
//            val imageUrl = downUri.toString()
//            imageList.add(imageUrl)
//            Toast.makeText(context, imageUrl, Toast.LENGTH_SHORT).show()
//            Log.d("Storage", imageUrl)
//        }.addOnFailureListener {
//            Log.d("Storage", "Error: $it")
//        }
//        ref.downloadUrl.addOnCompleteListener {
//            OnCompleteListener {
//                if (it.isSuccessful) {
//                    val downUri: Uri = it.result
//                    val imageUrl = downUri.toString()
//                    imageList.add(imageUrl)
//                    Toast.makeText(context, imageUrl, Toast.LENGTH_SHORT).show()
//                    Log.d("Storage", imageUrl)
//                } else {
//                    Log.d("Storage", "Error")
//                }
//            }
//        }

        Log.d("Storage", imageList.toString())

//        val boat = arguments?.getParcelable<Boat>("boat")
//        Log.d("BOAT", boat.toString())
//        Log.d("BOAT_VIEW_MODEL", boatViewModel.boat.toString())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddAnnouncementBinding.inflate(inflater, container, false)
        btnSave = binding.btnSave
        etPort = binding.etPort
        checkBoxLicense = binding.checkBoxLicense
        checkBoxCaptain = binding.checkBoxCaptainNeeded
        rvImages = binding.rvImages
        rvServices = binding.rvServices
        etDescription = binding.etDescription

        btnAddImage = binding.btnAddImage
        btnAddService = binding.btnAddService

        rvImages.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvImages.adapter = imageAdapter

        rvServices.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvServices.adapter = serviceAdapter

        btnAddImage.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                startActivityForResult(it, 0)
            }
        }

        binding.btnAddService.setOnClickListener {
            serviceList.add(BoatService("Nuovo servizio", 0))
            serviceAdapter.notifyDataSetChanged()
        }

        binding.backBtn.setOnClickListener {
            val action =
                AddAnnouncementFragmentDirections.actionAddAnnouncementFragmentToAddBoatFragment()
            findNavController().navigate(action)
        }

        btnSave.setOnClickListener {
            if (validatePort()) {
                // Announcement ID
                val AID = Util.getUID().plus("@${Timestamp.now().seconds}")
                val UID = Util.getUID()

                val announcement = Announcement(
                    id = AID,
                    id_owner = UID,
                    announce_name = boatViewModel.boat!!.name.toString(),
                    boat = boatViewModel.boat!!,
                    capt_needed = checkBoxCaptain.isChecked,
                    licence_needed = checkBoxLicense.isChecked,
                    location = etPort.text.toString(),
                    description = "Description",
                    services = arrayListOf(),
                    imageList =arrayListOf(),
                    available = true
                )

//                val boatDocument = hashMapOf(
//                    "builder" to announcement.boat!!.builder,
//                    "model" to announcement.boat!!.model,
//                    "year" to announcement.boat!!.year,
//                    "length" to announcement.boat!!.length,
//                    "passengers" to announcement.boat!!.passengers,
//                    "license" to announcement.boat!!.license
//                )
//
//                val announcementDocument = hashMapOf(
//                    "name" to announcement.name,
//                    "id" to id,
//                    "location" to announcement.location,
//                    "description" to announcement.description,
//                    "imageList" to announcement.imageList,
//                    "services" to announcement.services,
//                    "available" to announcement.available
//                )

//                announcementDocument["boat"] = boatDocument

                // Store on Firestore
                /* fDatabase.collection(Util.getUID()!!)
                    .document(id)
                    .set(announcement)

                 */

                announceViewModel.addAnnouncementToDatabase(announcement,AID)

                /*fDatabase.collection("BoatAnnouncement")
                    .document(aID)
                    .set(announcement)

                 */

//                fDatabase.collection(Util.getUID()!!)
//                    .document(id)
//                    .set(announcementDocument)
//                    .addOnSuccessListener { Log.d("Firestore", "AnnouncementDocument added!") }
//                    .addOnFailureListener { e -> Log.d("Firestore", "Error: $e") }

//                Log.d("boat", "ANNOUNCEMENT: $announcement")
//                Log.d("boat", "BOAT (ViewModel): \n ${boatViewModel.boat.toString()}")
//                boatViewModel.setBoat(boat, yearPosition, lengthPosition)

//                Util.mDatabase.child("boats").child(Util.getUID().toString()).child(id)
//                    .setValue(boatViewModel.boat!!).addOnSuccessListener {
//                        Toast.makeText(
//                            context,
//                            "Barca aggiunta correttamente!",
//                            Toast.LENGTH_SHORT
//                        )
//                    }

                // Save the announcement on Realtime Database (Firebase)
//                Util.mDatabase.child("announcements").child(Util.getUID().toString()).child(id)
//                    .setValue(announcement).addOnSuccessListener {
//                        Toast.makeText(
//                            context,
//                            "Annuncio aggiunto correttamente!",
//                            Toast.LENGTH_SHORT
//                        ).show()
//
//                        // Reset BoatViewModel
//                        boatViewModel.setBoat(null, 0, 0)

                val action =
                    AddAnnouncementFragmentDirections.actionAddAnnouncementFragmentToMyAnnouncementsFragment()
                findNavController().navigate(action)

                // Can be removed
//                val bundle = Bundle()
//                bundle.putParcelable("boat", boat)
//                findNavController().navigate(R.id.addAnnouncementFragment, bundle)
//                Toast.makeText(context, "Aggiungi", Toast.LENGTH_SHORT).show()

            }

        }

        return binding.root
    }

    private fun validatePort(): Boolean {
        return if (etPort.text.toString().trim().isEmpty()) {
            binding.textInputLayoutPort.isErrorEnabled = true
            binding.textInputLayoutPort.error = getString(R.string.required_field)
            etPort.requestFocus()
            false
        } else {
            binding.textInputLayoutPort.isErrorEnabled = false
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 0) {
            val uri = data?.data
            imageList.add(uri.toString())
            imageAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment AddAnnouncementFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddAnnouncementFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}