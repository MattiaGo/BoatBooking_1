package com.example.boatbooking_1.ui.auth

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.boatbooking_1.R
import com.example.boatbooking_1.databinding.FragmentUserProfileBinding
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_EMAIL = "email"
private const val ARG_NAME = "name"

class UserProfileFragment : Fragment() {

    private lateinit var userProfileBinding: FragmentUserProfileBinding
    private lateinit var fAuth: FirebaseAuth
    private lateinit var btnLogout: Button
    private lateinit var etEmail: EditText
    private lateinit var etName: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        userProfileBinding = FragmentUserProfileBinding.inflate(inflater, container, false)
        btnLogout = userProfileBinding.btnLogout
        etEmail = userProfileBinding.etEmail
        etName = userProfileBinding.etName

        etEmail.setText(arguments?.getString(ARG_EMAIL))
        etName.setText(arguments?.getString(ARG_NAME))

        return (userProfileBinding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLogout.setOnClickListener {
            fAuth.signOut()

            val action = UserProfileFragmentDirections.actionUserProfileToMainAccount()
            findNavController().navigate(action)

            Toast.makeText(context, "Logout effettuato con successo!", Toast.LENGTH_SHORT).show()
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserProfileFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}