package com.example.boatbooking_1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.boatbooking_1.databinding.FragmentAddAnnouncementBinding
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.model.Boat
import com.example.boatbooking_1.utils.Util
import com.example.boatbooking_1.viewmodel.AnnouncementViewModel
import com.example.boatbooking_1.viewmodel.BoatViewModel
import com.google.android.material.textfield.TextInputEditText

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
    private lateinit var etName: TextInputEditText
    private lateinit var etPort: TextInputEditText
    private lateinit var etDescription: TextInputEditText
    private lateinit var rvImages: RecyclerView
    private lateinit var rvServices: RecyclerView

    private val boatViewModel: BoatViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }

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
        etName = binding.etName
        etPort = binding.etPort
        rvImages = binding.rvImages
        rvServices = binding.rvServices
        etDescription = binding.etDescription

        binding.btnBack.setOnClickListener {
            val action =
                AddAnnouncementFragmentDirections.actionAddAnnouncementFragmentToAddBoatFragment()
            findNavController().navigate(action)
        }

        btnSave.setOnClickListener {
            if (validatePort().and(validateName())) {
                val announcement = Announcement(
                    boat = boatViewModel.boat!!,
                    etName.text.toString(),
                    Util.getUID(),
                    etPort.text.toString(),
                    "Description",
                    arrayListOf(),
                    arrayListOf(),
                    true
                )

//                boatViewModel.setBoat(boat, yearPosition, lengthPosition)

                // Save the announcement on Realtime Database (Firebase)
                Util.mDatabase.child("announcements").child(Util.getUID().toString()).push()
                    .setValue(announcement).addOnSuccessListener {
                        // Reset BoatViewModel
                        boatViewModel.setBoat(null, 0, 0)

                        Toast.makeText(context, "Annuncio aggiunto correttamente!", Toast.LENGTH_SHORT)

                        val action =
                            AddAnnouncementFragmentDirections.actionAddAnnouncementFragmentToUserProfile()
                        findNavController().navigate(action)
                    }

                // Can be removed
//                val bundle = Bundle()
//                bundle.putParcelable("boat", boat)
//                findNavController().navigate(R.id.addAnnouncementFragment, bundle)
//                Toast.makeText(context, "Aggiungi", Toast.LENGTH_SHORT).show()

            }

        }

        return binding.root
    }

    private fun validateName(): Boolean {
        return if (etName.text.toString().trim().isEmpty()) {
            binding.textInputLayoutName.isErrorEnabled = true
            binding.textInputLayoutName.error = getString(R.string.required_field)
            etName.requestFocus()
            false
        } else {
            binding.textInputLayoutName.isErrorEnabled = false
            true
        }
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