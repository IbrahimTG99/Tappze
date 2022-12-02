package com.devsinc.tappze.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.devsinc.tappze.R
import com.devsinc.tappze.data.Resource
import com.devsinc.tappze.databinding.FragmentSettingsBinding
import com.devsinc.tappze.ui.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BindingFragment<FragmentSettingsBinding>() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private val viewModel: SettingsViewModel by viewModels()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSettingsBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvSignOut.setOnClickListener {
            viewModel.logout()

            lifecycleScope.launchWhenStarted {
                viewModel.getDatabaseFlow.collect { event ->
                    when (event) {
                        is Resource.Success -> {
                            findNavController().navigate(R.id.action_settingsFragment_to_welcomeFragment)

                            // disable bottom navigation in main activity
                            requireActivity().findViewById<View>(R.id.bottom_nav_view).visibility =
                                View.GONE
                            findNavController().popBackStack(R.id.welcomeFragment, false)
                        }
                        is Resource.Error -> {
                            Toast.makeText(
                                requireContext(),
                                "Error: ${event.exception.message.toString()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> {}
                    }
                }
            }

        }

        binding.toggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.btn_profile_on -> {
                        viewModel.updateProfileStatus(true)
                    }
                    R.id.btn_profile_off -> {
                        viewModel.updateProfileStatus(false)
                    }
                }
            }

            lifecycleScope.launchWhenStarted {
                viewModel.getStatusFlow.collect {
                    when (it) {
                        is Resource.Success -> {
                            if (it.result) {
                                binding.toggleGroup.check(R.id.btn_profile_on)
                            } else {
                                binding.toggleGroup.check(R.id.btn_profile_off)
                            }
                        }
                        is Resource.Error -> {
                            Toast.makeText(
                                requireContext(),
                                "Error: ${it.exception.message.toString()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> {}
                    }
                }
            }
        }

    }
}