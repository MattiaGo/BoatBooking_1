package com.example.boatbooking_1.ui.auth

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.boatbooking_1.R
import com.example.boatbooking_1.databinding.FragmentUserProfileBinding
import com.example.boatbooking_1.model.User
import com.example.boatbooking_1.utils.Util
import com.example.boatbooking_1.utils.Util.hideKeyboard
import com.example.boatbooking_1.viewmodel.MyBookingViewModel
import com.example.boatbooking_1.viewmodel.UserProfileViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText


private const val ARG_EMAIL = "email"
private const val ARG_NAME = "name"

class UserProfileFragment : Fragment() {

    private lateinit var binding: FragmentUserProfileBinding

    private val userProfileViewModel: UserProfileViewModel by activityViewModels()
    private val myBookingsViewModel: MyBookingViewModel by activityViewModels()

    private lateinit var imageUri: Uri

    //private lateinit var permissions: Permissions
    private lateinit var alertDialog: AlertDialog
    private lateinit var user: User
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesEdit: SharedPreferences.Editor


    private lateinit var btnAddBoat: Button
    private lateinit var etEmail: TextInputEditText
    private lateinit var etName: TextInputEditText

//    private var isOwner: Boolean = false

    private lateinit var observer: Observer<User?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        disableOnBackClick()

        Util.fDatabase.collection("BoatBookings")
            .document(Util.getUID()!!)
            .set(
                hashMapOf(
                    "id" to Util.getUID()
                )
            )

        /*mAuthListener = AuthStateListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                Toast.makeText(context, "already logged", Toast.LENGTH_SHORT).show()
            }
        }*/
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        sharedPreferences =
            context.getSharedPreferences("UserInfo&Preferences", Context.MODE_PRIVATE)
        sharedPreferencesEdit = sharedPreferences.edit()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        etEmail = binding.etEmail
        etName = binding.etName
        binding.myBoatBtn.isVisible = false
        binding.btnSave.visibility = View.GONE

//        ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application).create(
//            UserProfileViewModel::class.java
//        )

        observer =
            Observer<User?> { userModel ->
                binding.user = userModel
                user = userModel
                val name: String? = userModel.name
                val email: String? = userModel.email

                binding.etName.setText(name)
                binding.etEmail.setText(email)

//                Log.d("userViewModel", user.toString())

                if (user.shipOwner) {
                    binding.profileImage.setImageResource(R.drawable.ic_shipowner)
                    binding.myBoatBtn.text = getString(R.string.my_boats)
                    binding.myBoatBtn.isVisible = true
//                    sharedPreferencesEdit.putBoolean("owner", true).apply() // Useless
                } else {
                    binding.myBoatBtn.text = getString(R.string.activate_owner_mode)
                    binding.myBoatBtn.isVisible = true
//                    sharedPreferencesEdit.putBoolean("owner", false).apply() // Useless
                }
                Log.d("MyBookings", user.toString())
            }

//        etName.setOnFocusChangeListener { _, hasFocus ->
//            if (!hasFocus) validateName()
//        }
//
//        etEmail.setOnFocusChangeListener { _, hasFocus ->
//            if (!hasFocus) validateEmail()
//        }

        val editText = arrayOf(etName, etEmail)
        for (et in editText) {

//            val et1 = etName.text.toString().trim()
//            val et2 = etEmail.text.toString().trim()

            et.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(
                    s: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    if (validateName() && validateEmail()) //&& (etName.text.toString() != user.name.toString() || etEmail.text.toString() != user.email.toString()))
                        binding.btnSave.visibility = View.VISIBLE
                    //else binding.btnSave.visibility = View.INVISIBLE

//                    if (et1.isNotEmpty()) {
//                        binding.textInputLayoutName.isErrorEnabled = false
//                    }
//                    if (et2.isNotEmpty()) {
//                        binding.textInputLayoutEmail.isErrorEnabled = false
//                    }
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

        userProfileViewModel.getUser().observe(viewLifecycleOwner, observer)

        return binding.root
    }


    /*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
                val name = data.getStringExtra("name")
                profileViewModel.edtUsername(name)
                sharedPreferences.putString("username", name).apply()
            }
    }
*/

    private fun showConfirmDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Sei sicuro di attivare la modalità PROPRIETARIO?")
            .setMessage("ATTENZIONE! QUESTA OPERAZIONE È IRREVERSIBILE")
            .setCancelable(false)
            .setNegativeButton("Annulla") { _, _ ->
            }
            .setPositiveButton("Conferma") { _, _ ->
                activateOwnerMode()
                Toast.makeText(
                    context,
                    "Modalità Proprietario attivata con successo!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .show()
    }

    private fun activateOwnerMode() {
        userProfileViewModel.editStatus(true)
//        binding.myBoatBtn.setText("Le mie barche")
    }


    private fun disableOnBackClick() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            // With blank your fragment BackPressed will be disabled.
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //firebaseAuth.addAuthStateListener(mAuthListener)

        binding.logoutBtn.setOnClickListener {
            signOut()
        }

        binding.btnMyBookings.setOnClickListener {
            val action =
                UserProfileFragmentDirections.actionUserProfileToMyBookingsFragment()
            findNavController().navigate(action)
        }

        binding.myBoatBtn.setOnClickListener {
            //val name = binding.etName.text.toString()
            //profileViewModel.edtUsername(name)
            if (user.shipOwner) {
                val action =
                    UserProfileFragmentDirections.actionUserProfileToMyAnnouncementsFragment()
                findNavController().navigate(action)
            } else {
                showConfirmDialog()
            }
        }

        binding.btnSave.setOnClickListener {
            if (validateName().and(validateEmail())) {
                userProfileViewModel.editName(etName.text.toString())
                userProfileViewModel.editEmail(etEmail.text.toString(), requireContext())
                etName.clearFocus()
                etEmail.clearFocus()
                hideKeyboard()
            }
        }
    }

    /*binding.myBoatBtn.setOnClickListener {
    val action = UserProfileFragmentDirections.actionUserProfileToAddBoatFragment()
    findNavController().navigate(R.id.account)
    findNavController().navigate(action)
}
 */

    private fun validateName(): Boolean {
        return if (etName.text.toString().trim().isEmpty()) {
            binding.textInputLayoutName.isErrorEnabled = true
            binding.textInputLayoutName.error = getString(R.string.required_field)
            false
        } else {
            binding.textInputLayoutName.isErrorEnabled = false
            true
        }
    }

    private fun validateEmail(): Boolean {
        return if (etEmail.text.toString().trim().isEmpty()) {
            binding.textInputLayoutEmail.isErrorEnabled = true
            binding.textInputLayoutEmail.error = getString(R.string.required_field)
            false
        } else {
            binding.textInputLayoutEmail.isErrorEnabled = false
            true
        }
    }

    private fun signOut() {
        val user = Util.firebaseAuth.currentUser

        if (user != null) {
            myBookingsViewModel.refresh()

            Util.firebaseAuth.signOut()
            val action = UserProfileFragmentDirections.actionUserProfileToMainAccount()
            findNavController().navigate(action)
        }

//        btnAddBoat.setOnClickListener {
//            val action = UserProfileFragmentDirections.actionUserProfileToAddBoatFragment()
//            findNavController().navigate(action)
//        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         */
        @JvmStatic
        fun newInstance(email: String?, name: String?) =
            UserProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_EMAIL, email)
                    putString(ARG_NAME, name)
                }
            }
    }
}
