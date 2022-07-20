package com.example.boatbooking_1.ui.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.boatbooking_1.LoginViewModel
import com.example.boatbooking_1.R
import com.example.boatbooking_1.databinding.*
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

class AccountFragment : Fragment() {

    // Get a reference to the ViewModel scoped to this Fragment
    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var binding: FragmentAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAuthenticationState()

        //binding.loginBtn.setOnClickListener {
        //    val action = .actionMainFragmentToSettingsFragment()
        //    findNavController().navigate(action)
        //}
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
                    //binding.welcomeText.text = getFactWithPersonalization(factToDisplay)

                    binding.loginBtn.text = getString(R.string.logout_button_text)
                    binding.loginBtn.setOnClickListener {
                        AuthUI.getInstance().signOut(requireContext())
                    }
                }
                else -> {
                    //binding.welcomeText.text = factToDisplay

                    binding.loginBtn.text = getString(R.string.login_button_text)
                    binding.loginBtn.setOnClickListener {
                        findNavController().navigate(R.id.login)
                    }
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