package com.devsinc.tappze.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.devsinc.tappze.R
import com.devsinc.tappze.databinding.FragmentWelcomeBinding
import com.devsinc.tappze.ui.BindingFragment

class WelcomeFragment : BindingFragment<FragmentWelcomeBinding>() {

    companion object {
        fun newInstance() = WelcomeFragment()
    }

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentWelcomeBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreateAccount.setOnClickListener {
            // move to sign up fragment
            findNavController().navigate(R.id.action_welcomeFragment_to_signUpFragment)
        }

        binding.btnSignIn.setOnClickListener {
            // move to sign in fragment
            findNavController().navigate(R.id.action_welcomeFragment_to_signInFragment)
        }
    }
}