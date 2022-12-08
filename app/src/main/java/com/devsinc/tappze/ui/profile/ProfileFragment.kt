package com.devsinc.tappze.ui.profile

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.devsinc.tappze.R
import com.devsinc.tappze.data.Resource
import com.devsinc.tappze.data.utils.Constants
import com.devsinc.tappze.databinding.FragmentProfileBinding
import com.devsinc.tappze.model.AppIcon
import com.devsinc.tappze.model.UserData
import com.devsinc.tappze.ui.BindingFragment
import com.devsinc.tappze.ui.alert.AlertFragment
import com.devsinc.tappze.ui.displayinfo.DisplayInfoFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BindingFragment<FragmentProfileBinding>() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var userData: UserData

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentProfileBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUserDatabase()
        binding.progressIndicator.visibility = View.VISIBLE
        lifecycleScope.launchWhenStarted {
            viewModel.getDatabaseFlow.collect { event ->
                when (event) {
                    is Resource.Success -> {
                        binding.tvFullname.text = event.result.fullName
                        binding.tvFullname.setTypeface(null, Typeface.BOLD)
                        userData = event.result
                        val profileAdapter = RecyclerAdapterProfile(userData.infoMap)
                        binding.rvProfilelinks.adapter = profileAdapter
                        profileAdapter.setOnItemClickListener(object :
                            RecyclerAdapterProfile.OnItemClickListener {
                            override fun onItemClick(position: Int) {
                                val displayInfoFragment =
                                    DisplayInfoFragment(userData.infoMap?.keys?.let {
                                        Constants.getImage(
                                            it.elementAt(position)
                                        )
                                    }?.let {
                                        AppIcon(
                                            it,
                                            userData.infoMap?.keys!!.elementAt(position)
                                        )
                                    })
                                displayInfoFragment.show(
                                    parentFragmentManager,
                                    DisplayInfoFragment.TAG
                                )
                            }
                        })
                        binding.progressIndicator.visibility = View.GONE
                        if (event.result.profileImage != null) {
                            Glide.with(this@ProfileFragment)
                                .load(event.result.profileImage)
                                .placeholder(R.drawable.ic_baseline_account_circle_24)
                                .error(R.drawable.ic_baseline_no_accounts_24)
                                .centerCrop()
                                .into(binding.profileImage)
                        }
                    }
                    is Resource.Error -> {
                        val dialog = AlertFragment(event, "Error Loading Profile")
                        dialog.show(parentFragmentManager, AlertFragment.TAG)
                    }
                    else -> {}
                }
            }
        }
        binding.btnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }
    }
}