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
            viewModel.signUp(
                binding.etName.text.toString(),
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            )
        }

        lifecycleScope.launchWhenStarted {
            viewModel.signUpFlow.collect {
                when (it) {
                    is Resource.Success -> {
                        Toast.makeText(requireContext(), "Sign Up Successful", Toast.LENGTH_SHORT)
                            .show()
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