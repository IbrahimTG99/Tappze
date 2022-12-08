package com.devsinc.tappze.ui.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.devsinc.tappze.R
import com.devsinc.tappze.data.Resource
import com.devsinc.tappze.data.utils.Constants
import com.devsinc.tappze.databinding.FragmentSignInBinding
import com.devsinc.tappze.ui.BindingFragment
import com.devsinc.tappze.ui.alert.AlertFragment
import com.devsinc.tappze.ui.forgotpass.ForgotPassFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BindingFragment<FragmentSignInBinding>() {

    companion object {
        fun newInstance() = SignInFragment()
    }

    private val viewModel: SignInViewModel by viewModels()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSignInBinding::inflate


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivBack.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        binding.tvSignUp.setOnClickListener {
            // navigate to sign up fragment using navigation component
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        binding.tvForgotPassword.setOnClickListener {
            // pop up forgot password modal bottom sheet
            val forgotPassBottomSheet = ForgotPassFragment()
            forgotPassBottomSheet.show(parentFragmentManager, ForgotPassFragment.TAG)
        }

        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            var error = false

            if (email.isEmpty()) {
                binding.etEmail.error = "Email is required"
                binding.etEmail.requestFocus()
                // open keyboard
                Constants.openKeyboard(binding.etEmail, requireContext())
                error = true
            }

            if (password.isEmpty()) {
                binding.etPassword.error = "Password is required"
                binding.etPassword.requestFocus()
                Constants.openKeyboard(binding.etPassword, requireContext())
                error = true
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.etEmail.error = "Invalid email"
                binding.etEmail.requestFocus()
                Constants.openKeyboard(binding.etEmail, requireContext())
                error = true
            }

            if (!error) {
                viewModel.login(email, password)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.loginFlow.collect {
                when (it) {
                    is Resource.Success -> {
//                        val msg = Resource.Success("Login Successful")
//                        val dialog = AlertFragment(msg, "Success")
//                        dialog.show(parentFragmentManager, AlertFragment.TAG)
                        findNavController().navigate(R.id.action_signInFragment_to_profileFragment)
                        // access bottom nav view and make it visible
                        requireActivity().findViewById<View>(R.id.bottom_nav_view).visibility =
                            View.VISIBLE
                        // close keyboard
                        Constants.closeKeyboard(view, requireContext())
                    }
                    is Resource.Error -> {
                        val dialog = AlertFragment(it, "Login Error")
                        dialog.show(parentFragmentManager, AlertFragment.TAG)
                        binding.progressBar.visibility = View.GONE
                    }
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE

                        // start loading animation
                        binding.progressBar.isIndeterminate = true
                    }
                    else -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }
}