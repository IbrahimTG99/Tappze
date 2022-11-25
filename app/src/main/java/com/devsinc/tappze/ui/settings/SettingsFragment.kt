package com.devsinc.tappze.ui.settings

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.devsinc.tappze.R
import com.devsinc.tappze.databinding.FragmentSettingsBinding
import com.devsinc.tappze.databinding.FragmentSignUpBinding
import com.devsinc.tappze.ui.BindingFragment
import com.devsinc.tappze.ui.auth.AuthViewModel

class SettingsFragment : BindingFragment<FragmentSettingsBinding>() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private lateinit var viewModel: SettingsViewModel

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSettingsBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]

        binding.tvSignOut.setOnClickListener {
            viewModel.logout()
            findNavController().navigate(R.id.action_settingsFragment_to_welcomeFragment)
        }

    }

}