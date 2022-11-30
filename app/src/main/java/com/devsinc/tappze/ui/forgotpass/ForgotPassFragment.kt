package com.devsinc.tappze.ui.forgotpass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.devsinc.tappze.R
import com.devsinc.tappze.data.Resource
import com.devsinc.tappze.databinding.FragmentForgotPassBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPassFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentForgotPassBinding

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    private lateinit var viewModel: ForgotPassViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_forgot_pass, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ForgotPassViewModel::class.java]
        binding = FragmentForgotPassBinding.bind(view)

        binding.btnSend.setOnClickListener {
            if (binding.etForgotPassEmail.text.toString().trim().isEmpty()) {
                binding.etForgotPassEmail.error = "Please enter a valid email address"
            } else {
                viewModel.resetPassword(binding.etForgotPassEmail.text.toString().trim())
            }
        }

        binding.ivClose.setOnClickListener {
            dismiss()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.resetFlow.collect { event ->
                when (event) {
                    is Resource.Success -> {
                        Toast.makeText(
                            requireContext(),
                            "Password reset email sent",
                            Toast.LENGTH_SHORT
                        ).show()
                        dismiss()
                    }
                    is Resource.Error -> {
                        Toast.makeText(
                            requireContext(),
                            "Error: ${event.exception.message.toString()}",
                            Toast.LENGTH_SHORT
                        ).show()
                        dismiss()
                    }
                    is Resource.Loading -> {
                        // do nothing
//                        Toast.makeText(requireContext(), "Password email on its way", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        // do nothing
                    }
                }
            }
        }
    }
}