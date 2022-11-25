package com.devsinc.tappze.ui.forgotpass

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devsinc.tappze.R
import com.devsinc.tappze.databinding.FragmentForgotPassBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ForgotPassFragment : BottomSheetDialogFragment() {

    private lateinit var binding : FragmentForgotPassBinding

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
            // send email to reset password
        }

        binding.ivClose.setOnClickListener {
            dismiss()
        }
    }
}