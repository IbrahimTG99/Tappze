package com.devsinc.tappze.ui.nfc

import android.graphics.Typeface
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.devsinc.tappze.R
import com.devsinc.tappze.data.Resource
import com.devsinc.tappze.databinding.FragmentNfcBinding
import com.devsinc.tappze.ui.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NfcFragment : BindingFragment<FragmentNfcBinding>() {

    companion object {
        fun newInstance() = NfcFragment()
    }

    private val viewModel: NfcViewModel by viewModels()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentNfcBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvUsername.text = viewModel.user?.displayName ?: "No user logged in"
        binding.tvUsername.setTypeface(null, Typeface.BOLD)

    }

}