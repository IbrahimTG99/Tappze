package com.devsinc.tappze.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.devsinc.tappze.R
import com.devsinc.tappze.data.Resource
import com.devsinc.tappze.data.utils.Constants
import com.devsinc.tappze.databinding.FragmentSignUpBinding
import com.devsinc.tappze.ui.BindingFragment
import com.devsinc.tappze.ui.alert.AlertFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BindingFragment<FragmentSignUpBinding>() {

    companion object {
        fun newInstance() = SignUpFragment()
    }

    private lateinit var viewModel: SignUpViewModel

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSignUpBinding::inflate


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]

        binding.ivBack.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        binding.btnSignUp.setOnClickListener {
            var error = false

            if (binding.etName.text.toString().isEmpty()) {
                binding.etName.error = "Name is required"
                binding.etName.requestFocus()
                // open keyboard
                Constants.openKeyboard(binding.etName, requireContext())
                error = true
            }

            if (binding.etUsername.text.toString().isEmpty() || !binding.etUsername.text.toString().matches(Regex("^[a-z0-9]*\$"))) {
                binding.etUsername.error = "Username is required and only a-z and 0-9 are allowed"
                error = true
                binding.etUsername.requestFocus()
                // open keyboard
                Constants.openKeyboard(binding.etUsername, requireContext())
            }

            if (binding.etEmail.text.toString().isEmpty()) {
                binding.etEmail.error = "Email is required"
                binding.etEmail.requestFocus()
                // open keyboard
                Constants.openKeyboard(binding.etEmail, requireContext())
                error = true
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString())
                    .matches()
            ) {
                binding.etEmail.error = "Please enter a valid email address"
                binding.etEmail.requestFocus()
                // open keyboard
                Constants.openKeyboard(binding.etEmail, requireContext())
                error = true
            }

            if (binding.etPassword.text.toString()
                    .isEmpty() || binding.etPassword.text.toString().length < 6
            ) {
                binding.etPassword.error = "Password must be at least 6 characters"
                binding.etPassword.requestFocus()
                // open keyboard
                Constants.openKeyboard(binding.etPassword, requireContext())
                error = true
            }

            if (binding.etConfirmPassword.text.toString().isEmpty()) {
                binding.etConfirmPassword.error = "Confirm password is required"
                binding.etConfirmPassword.requestFocus()
                // open keyboard
                Constants.openKeyboard(binding.etConfirmPassword, requireContext())
                error = true
            }

            if (binding.etPassword.text.toString() != binding.etConfirmPassword.text.toString()) {
                binding.etConfirmPassword.error = "Passwords do not match"
                binding.etConfirmPassword.requestFocus()
                // open keyboard
                Constants.openKeyboard(binding.etConfirmPassword, requireContext())
                error = true
            }

            if (!error) {
                viewModel.signUp(
                    binding.etEmail.text.toString().trim(),
                    binding.etPassword.text.toString().trim(),
                    binding.etUsername.text.toString().trim()
                )
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.signUpFlow.collect {
                when (it) {
                    is Resource.Success -> {
//                        Toast.makeText(requireContext(), "Sign Up Successful", Toast.LENGTH_SHORT)
//                            .show()
                        val msg = Resource.Success("Signup Successful")
                        val dialog = AlertFragment(msg, "Success")
                        dialog.show(parentFragmentManager, AlertFragment.TAG)

                        // add user data to db
                        viewModel.addUserToDatabase(binding.etName.text.toString())
                        findNavController().navigate(R.id.profileFragment)
                        requireActivity().findViewById<View>(R.id.bottom_nav_view).visibility =
                            View.VISIBLE
                        // close keyboard
                        Constants.closeKeyboard(view, requireContext())
                    }
                    is Resource.Error -> {
                        val dialog = AlertFragment(it, "Signup Error")
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