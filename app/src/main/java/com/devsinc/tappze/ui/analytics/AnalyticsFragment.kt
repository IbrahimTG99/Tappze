package com.devsinc.tappze.ui.analytics

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
import com.bumptech.glide.Glide
import com.devsinc.tappze.R
import com.devsinc.tappze.data.Resource
import com.devsinc.tappze.databinding.FragmentAnalyticsBinding
import com.devsinc.tappze.ui.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnalyticsFragment : BindingFragment<FragmentAnalyticsBinding>() {

    companion object {
        fun newInstance() = AnalyticsFragment()
    }

    private val viewModel: AnalyticsViewModel by viewModels()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentAnalyticsBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUserDatabase()

        lifecycleScope.launchWhenStarted {
            viewModel.getDatabaseFlow.collect { event ->
                when (event) {
                    is Resource.Success -> {
                        val user = event.result
                        if (user.profileImage != null) {
                            Glide.with(this@AnalyticsFragment)
                                .load(user.profileImage)
                                .placeholder(R.drawable.ic_baseline_account_circle_24)
                                .into(binding.profileImage)
                        }
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
}