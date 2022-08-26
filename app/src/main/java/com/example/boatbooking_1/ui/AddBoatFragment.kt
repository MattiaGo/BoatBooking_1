package com.example.boatbooking_1.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.boatbooking_1.R
import com.example.boatbooking_1.databinding.FragmentAddBoatBinding
import com.example.boatbooking_1.model.Boat
import com.example.boatbooking_1.viewmodel.BoatViewModel
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.TextInputEditText


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddBoatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddBoatFragment : Fragment() {
    private lateinit var binding: FragmentAddBoatBinding
    private lateinit var etBoatName: TextInputEditText
    private lateinit var etModel: TextInputEditText
    private lateinit var etBuilder: TextInputEditText
    private lateinit var spinnerYear: Spinner
    private lateinit var spinnerLength: Spinner
    private lateinit var sliderYear: Slider
    private lateinit var sliderLength: Slider
    private lateinit var sliderPassengers: Slider
    private lateinit var sliderBeds: Slider
    private lateinit var sliderCabins: Slider
    private lateinit var sliderBathrooms: Slider



    private lateinit var checkBoxLicense: CheckBox

    private val boatViewModel: BoatViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentAddBoatBinding.inflate(inflater, container, false)

        etBoatName = binding.etBoatName
        etModel = binding.etModel
        etBuilder = binding.etBuilder
        sliderYear = binding.sliderYear//spinnerYear = binding.spinnerYear
        sliderLength = binding.sliderLength//spinnerLength = binding.spinnerLength
        sliderPassengers = binding.sliderMaxPassengers
        sliderBeds = binding.sliderBeds
        sliderCabins = binding.sliderCabins
        sliderBathrooms = binding.sliderBathroom
        //checkBoxLicense = binding.checkBoxLicense

        if (boatViewModel.boat != null) {
            etBoatName.setText(boatViewModel.boat!!.name)
            etModel.setText(boatViewModel.boat!!.model)
            etBuilder.setText(boatViewModel.boat!!.builder)
            sliderYear.value = boatViewModel.boat!!.year!!.toFloat()                //binding.spinnerYear.setSelection(boatViewModel.yearPosition)
            sliderLength.value = boatViewModel.boat!!.length!!.toFloat()            //binding.spinnerLength.setSelection(boatViewModel.lengthPosition)
            sliderPassengers.value = boatViewModel.boat!!.passengers!!.toFloat()    //binding.sliderMaxPassengers.value = boatViewModel.boat!!.passengers!!.toFloat()
            sliderBeds.value = boatViewModel.boat!!.beds!!.toFloat()
            sliderCabins.value = boatViewModel.boat!!.cabins!!.toFloat()
            sliderBathrooms.value = boatViewModel.boat!!.bathrooms!!.toFloat()

            //binding.checkBoxLicense.isChecked = boatViewModel.boat!!.license!!

        }

//        if (validateBuilder() && validateModel()) {

        return binding.root
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupForm()
//        binding.btnAdd.setOnClickListener {
//            Toast.makeText(context, "CLICK!", Toast.LENGTH_SHORT).show()
//            // TODO: Firestore Database actions
//        }
        binding.backBtn.setOnClickListener {
            //boatViewModel.setBoat(null, 0, 0)
            boatViewModel.setBoat(null)
            val action = AddBoatFragmentDirections.actionAddBoatFragmentToMyAnnouncementsFragment()
            findNavController().navigate(action)
        }
    }

    private fun setupForm() {
//        btnAdd.isEnabled = false
//        btnAdd.alpha = 0.8f

        val editText = arrayOf(etModel, etBoatName,etBuilder)
        for (et in editText) {

            val et1 = etBoatName.text.toString().trim()
            val et2 = etModel.text.toString().trim()
            val et3 = etBuilder.text.toString().trim()

            et.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(
                    s: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    if (et1.isNotEmpty()) binding.textInputLayoutBoatName.isErrorEnabled = false
                    if (et2.isNotEmpty()) binding.textInputLayoutModel.isErrorEnabled = false
                    if (et3.isNotEmpty()) binding.textInputLayoutBuilder.isErrorEnabled = false
                }

                override fun beforeTextChanged(
                    s: CharSequence, start: Int, count: Int, after: Int
                ) {
                }

                override fun afterTextChanged(
                    s: Editable
                ) {
                }
            })
        }

        binding.goOnBtn.setOnClickListener {
            if (validateModel().and(validateBoatName()).and(validateBuilder())) {
                val boat = Boat(
                    name = etBoatName.text.toString(),
                    model = etModel.text.toString(),
                    builder = etBuilder.text.toString(),
                    year = sliderYear.value.toInt(),
                    length = sliderLength.value.toInt(),
                    passengers = sliderPassengers.value.toInt(),
                    beds = sliderBeds.value.toInt(),
                    cabins = sliderCabins.value.toInt(),
                    bathrooms = sliderBathrooms.value.toInt()



                    //spinnerYear.selectedItem.toString().toInt(),
                    //spinnerLength.selectedItem.toString().toInt(),
                    //checkBoxLicense.isChecked
                )

                //val yearPosition = spinnerYear.selectedItemPosition
                //val lengthPosition = spinnerLength.selectedItemPosition

                //boatViewModel.setBoat(boat, yearPosition, lengthPosition)

                boatViewModel.setBoat(boat)

                // Not necessary
//                val bundle = Bundle()
//                bundle.putParcelable("boat", boat)
//                findNavController().navigate(R.id.addAnnouncementFragment, bundle)
                findNavController().navigate(R.id.addAnnouncementFragment)
//                Toast.makeText(context, "Aggiungi", Toast.LENGTH_SHORT).show()

            }
        }
    }

//    override fun onViewStateRestored(savedInstanceState: Bundle?) {
//        super.onViewStateRestored(savedInstanceState)
//        val boat = savedInstanceState?.getParcelable<Boat>("boat")
//
//        if (boat != null) {
//            etBuilder.setText(boat.builder)
//            etModel.setText(boat.model)
//            spinnerLength.selectedItem.toString().toInt()
//            sliderPassengers.value = boat.passengers.toFloat()
//            checkBoxLicense.isChecked = boat.license
//        }
//    }

    private fun validateBoatName(): Boolean {
        return if (etBoatName.text.toString().trim().isEmpty()) {
            binding.textInputLayoutBoatName.isErrorEnabled = true
            binding.textInputLayoutBoatName.error = getString(R.string.required_field)
            etBoatName.requestFocus()
            false
        } else {
            binding.textInputLayoutBoatName.isErrorEnabled = false
            true
        }
    }

    private fun validateModel(): Boolean {
        return if (etModel.text.toString().trim().isEmpty()) {
            binding.textInputLayoutModel.isErrorEnabled = true
            binding.textInputLayoutModel.error = getString(R.string.required_field)
            etModel.requestFocus()
            false
        } else {
            binding.textInputLayoutModel.isErrorEnabled = false
            true
        }
    }

    private fun validateBuilder(): Boolean {
        return if (etBuilder.text.toString().trim().isEmpty()) {
            binding.textInputLayoutBuilder.isErrorEnabled = true
            binding.textInputLayoutBuilder.error = getString(R.string.required_field)
            etBuilder.requestFocus()
            false
        } else {
            binding.textInputLayoutBuilder.isErrorEnabled = false
            true
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddBoatFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddBoatFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    /**
     * Applying text watcher on each text field
     */
//    inner class TextFieldValidation(private val view: View) : TextWatcher {
//        override fun afterTextChanged(s: Editable?) {}
//        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            // checking ids of each text field and applying functions accordingly.
//            when (view.id) {
//                R.id.et_builder -> {
//                    validateBuilder()
//                }
//                R.id.et_model -> {
//                    validateModel()
//                }
//            }
//        }
//    }
}