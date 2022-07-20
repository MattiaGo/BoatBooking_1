package com.example.boatbooking_1.ui.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.boatbooking_1.LoginViewModel
import com.example.boatbooking_1.R
import com.example.boatbooking_1.databinding.FragmentAccountBinding
import com.example.boatbooking_1.databinding.FragmentUserProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AccountFragment : Fragment() {

    // Get a reference to the ViewModel scoped to this Fragment
    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var binding: FragmentAccountBinding
    private lateinit var userProfileBinding: FragmentUserProfileBinding


    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var fAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        //observeAuthenticationState()
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(inflater, container, false)

        username = binding.textEmailAddress
        password = binding.textPassword
        fAuth = Firebase.auth


        return binding.root
    }

    private fun firebaseSignIn() {
        binding.loginBtn.isEnabled = false
        binding.loginBtn.alpha = 0.5f

        fAuth.signInWithEmailAndPassword(username.text.toString(), password.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val action = AccountFragmentDirections.actionMainAccountToUserProfile()
                    findNavController().navigate(action)
                } else {
                    binding.loginBtn.isEnabled = true
                    binding.loginBtn.alpha = 1.0f
                    Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAuthenticationState()

        binding.loginBtn.setOnClickListener {
            firebaseSignIn()
        }

        binding.pwdForget.setOnClickListener {
            val action = AccountFragmentDirections.actionMainAccountToPasswordResetFragment()
            findNavController().navigate(action)
        }

        binding.signUpBtn.setOnClickListener {
            val action = AccountFragmentDirections.actionMainAccountToRegistration()
            findNavController().navigate(action)
        }
    }


    /**
     * Observes the authentication state and changes the UI accordingly.
     * If there is a logged in user: (1) show a logout button and (2) display their name.
     * If there is no logged in user: show a login button
     */
    private fun observeAuthenticationState() {
        //val factToDisplay = viewModel.getFactToDisplay(requireContext())

        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when (authenticationState) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> {

                    //findNavController().navigate(R.id.ic_account)
                    //binding.loginBtn.text = getString(R.string.logout_button_text)
                    //binding.loginBtn.setOnClickListener {
                    //    AuthUI.getInstance().signOut(requireContext())
                    //}
                }
                else -> {
                    //binding.welcomeText.text = factToDisplay

                    //binding.loginBtn.text = getString(R.string.login_button_text)
                    // binding.loginBtn.setOnClickListener {
                    //    findNavController().navigate(R.id.userProfile)
                    //}
                }
            }
        })
    }

    private fun getFactWithPersonalization(fact: String): String {
        return String.format(
            resources.getString(
                R.string.welcome_message_authed,
                FirebaseAuth.getInstance().currentUser?.displayName,
                Character.toLowerCase(fact[0]) + fact.substring(1)
            )
        )
    }
}