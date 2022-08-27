package com.example.boatbooking_1.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import androidx.compose.ui.text.toUpperCase
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.boatbooking_1.R
import com.example.boatbooking_1.adapter.ImageAdapter
import com.example.boatbooking_1.adapter.ServiceAdapter
import com.example.boatbooking_1.databinding.FragmentAddAnnouncementBinding
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.model.BoatService
import com.example.boatbooking_1.utils.Util
import com.example.boatbooking_1.viewmodel.AnnouncementViewModel
import com.example.boatbooking_1.viewmodel.BoatViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Timestamp
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [AddAnnouncementFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddAnnouncementFragment : Fragment() {
    /* TODO: Using the activityViewModels() Kotlin property delegate from the fragment-ktx artifact
        to retrieve the ViewModel in the activity scope
     */

    private lateinit var binding: FragmentAddAnnouncementBinding
    private lateinit var btnSave: Button
    private lateinit var etPort: TextInputEditText
    private lateinit var checkBoxLicense: CheckBox
    private lateinit var checkBoxCaptain: CheckBox
    private lateinit var etDescription: TextInputEditText
    private lateinit var etPrice: TextInputEditText
    private lateinit var rvImages: RecyclerView
    private lateinit var rvServices: RecyclerView
    private lateinit var btnAddImage: ImageButton
    private lateinit var btnAddService: ImageButton

    private lateinit var serviceAdapter: ServiceAdapter
    private lateinit var imageAdapter: ImageAdapter

    private lateinit var imageList: ArrayList<String>
    private lateinit var serviceList: ArrayList<BoatService>

    private val boatViewModel: BoatViewModel by activityViewModels()
    private val announceViewModel: AnnouncementViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageList = ArrayList()
        serviceList = ArrayList()

        imageAdapter = ImageAdapter(requireContext(), imageList, arrayListOf(), arrayListOf())
        serviceAdapter = ServiceAdapter(serviceList)

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
        etPrice = binding.etPrice

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
            serviceList.add(BoatService("Nuovo servizio", "0"))
            serviceAdapter.notifyDataSetChanged()
        }

        binding.backBtn.setOnClickListener {
            val action =
                AddAnnouncementFragmentDirections.actionAddAnnouncementFragmentToAddBoatFragment()
            findNavController().navigate(action)
        }

//        Log.d("Context", childFragmentManager.toString())

        btnSave.setOnClickListener {
            if (validatePort() && validatePrice()) {
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
                    location = etPort.text.toString().uppercase(Locale.getDefault()),
                    description = etDescription.text.toString(),
                    services = serviceList,
                    imageList = imageList,
                    price = etPrice.text.toString().toInt(),
                    available = true,
                    timestamp = Timestamp.now()
                )

                announceViewModel.addAnnouncementToDatabase(announcement, AID, requireContext())

                // Reset BoatViewModel
                boatViewModel.setBoat(null)

                val action =
                    AddAnnouncementFragmentDirections.actionAddAnnouncementFragmentToMyAnnouncementsFragment()
                findNavController().navigate(action)

                // Can be removed
//                val bundle = Bundle()
//                bundle.putParcelable("boat", boat)
//                findNavController().navigate(R.id.addAnnouncementFragment, bundle)
            }

        }

        return binding.root
    }


//    private fun validateName(): Boolean {
//        return if (etName.text.toString().trim().isEmpty()) {
//            binding.textInputLayoutName.isErrorEnabled = true
//            binding.textInputLayoutName.error = getString(R.string.required_field)
//            etName.requestFocus()
//            false
//        } else {
//            binding.textInputLayoutName.isErrorEnabled = false
//            true
//        }
//    }

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

    private fun validatePrice(): Boolean {
        return if (etPrice.text.toString().trim().isEmpty()) {
            binding.textInputLayoutPrice.isErrorEnabled = true
            binding.textInputLayoutPrice.error = getString(R.string.required_field)
            etPrice.requestFocus()
            false
        } else {
            binding.textInputLayoutPrice.isErrorEnabled = false
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 0) {
            val uri = data?.data!!
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