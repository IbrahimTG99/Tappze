package com.devsinc.tappze.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.devsinc.tappze.R
import com.devsinc.tappze.data.Resource
import com.devsinc.tappze.databinding.FragmentSignUpBinding
import com.devsinc.tappze.ui.BindingFragment
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
                error = true
            }

            if (binding.etUsername.text.toString().isEmpty()) {
                binding.etUsername.error = "Username is required"
                error = true
            }

            if (binding.etEmail.text.toString().isEmpty()) {
                binding.etEmail.error = "Email is required"
                error = true
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString())
                    .matches()
            ) {
                binding.etEmail.error = "Please enter a valid email address"
                error = true
            }

            if (binding.etPassword.text.toString()
                    .isEmpty() || binding.etPassword.text.toString().length < 6
            ) {
                binding.etPassword.error = "Password must be at least 6 characters"
                error = true
            }

            if (binding.etConfirmPassword.text.toString().isEmpty()) {
                binding.etConfirmPassword.error = "Confirm password is required"
                error = true
            }

            if (binding.etPassword.text.toString() != binding.etConfirmPassword.text.toString()) {
                binding.etConfirmPassword.error = "Passwords do not match"
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
                        Toast.makeText(requireContext(), "Sign Up Successful", Toast.LENGTH_SHORT)
                            .show()

                        // add user data to db
                        viewModel.addUserToDatabase(binding.etName.text.toString())
                        findNavController().navigate(R.id.profileFragment)
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), "Sign Up Failed", Toast.LENGTH_SHORT)
                            .show()
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