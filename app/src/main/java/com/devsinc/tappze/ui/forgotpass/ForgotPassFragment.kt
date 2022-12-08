package com.devsinc.tappze.ui.forgotpass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.devsinc.tappze.R
import com.devsinc.tappze.data.Resource
import com.devsinc.tappze.databinding.FragmentForgotPassBinding
import com.devsinc.tappze.ui.alert.AlertFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPassFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentForgotPassBinding
    private val viewModel: ForgotPassViewModel by viewModels()

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_forgot_pass, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                        val msg = Resource.Success("Password reset email sent")
                        val dialog = AlertFragment(msg, "Success")
                        dialog.show(parentFragmentManager, AlertFragment.TAG)
                        dismiss()
                    }
                    is Resource.Error -> {
                        val dialog = AlertFragment(event, "Error")
                        dialog.show(parentFragmentManager, AlertFragment.TAG)
                        dismiss()
                    }
                    is Resource.Loading -> {
                        // do nothing
                    }
                    else -> {
                        // do nothing
                    }
                }
            }
        }
    }
}