package com.devsinc.tappze.ui.signin

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
import com.devsinc.tappze.databinding.FragmentSignInBinding
import com.devsinc.tappze.ui.BindingFragment
import com.devsinc.tappze.ui.forgotpass.ForgotPassFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BindingFragment<FragmentSignInBinding>() {

    companion object {
        fun newInstance() = SignInFragment()
    }

    private lateinit var viewModel: SignInViewModel

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSignInBinding::inflate


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[SignInViewModel::class.java]

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
                error = true
            }

            if (password.isEmpty()) {
                binding.etPassword.error = "Password is required"
                error = true
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.etEmail.error = "Invalid email"
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
                        Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT)
                            .show()
                        findNavController().navigate(R.id.action_signInFragment_to_profileFragment)
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), "Login Failed", Toast.LENGTH_SHORT).show()
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