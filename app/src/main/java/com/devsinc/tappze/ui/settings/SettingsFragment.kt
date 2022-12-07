package com.devsinc.tappze.ui.settings

import android.content.Intent
import android.net.Uri
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
import com.devsinc.tappze.ui.alert.AlertFragment
import com.devsinc.tappze.ui.forgotpass.ForgotPassFragment
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

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

        viewModel.getProfileStatus()

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

        binding.tvPurchase.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://tappze.com/"))
            startActivity(intent)
        }

        binding.tvChangePassword.setOnClickListener {
            // pop up forgot password modal bottom sheet
            val forgotPassBottomSheet = ForgotPassFragment()
            forgotPassBottomSheet.show(parentFragmentManager, ForgotPassFragment.TAG)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.getStatusFlow.collect {
                when (it) {
                    is Resource.Success -> {
                        binding.toggleGroup.removeOnButtonCheckedListener { _, _, _ -> }
                        if (it.result) {
                            binding.toggleGroup.check(R.id.btn_profile_on)
                        } else {
                            binding.toggleGroup.check(R.id.btn_profile_off)
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

        lifecycleScope.launchWhenStarted {
            viewModel.setStatusFlow.collect {
                when (it) {
                    is Resource.Success -> {
                        if (it.result) {
                            binding.toggleGroup.check(R.id.btn_profile_on)
                            val msg = Resource.Success("Your Profile is now visible.")
                            val dialog = AlertFragment(msg, getString(R.string.alert))
                            dialog.show(parentFragmentManager, AlertFragment.TAG)

                        } else {
                            binding.toggleGroup.check(R.id.btn_profile_off)
                            val msg = Resource.Error(Exception("Your Profile is now invisible."))
                            val dialog = AlertFragment(msg, getString(R.string.alert))
                            dialog.show(parentFragmentManager, AlertFragment.TAG)
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