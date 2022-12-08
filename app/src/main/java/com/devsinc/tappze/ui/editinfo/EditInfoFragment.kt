package com.devsinc.tappze.ui.editinfo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.devsinc.tappze.R
import com.devsinc.tappze.data.Resource
import com.devsinc.tappze.data.utils.Constants
import com.devsinc.tappze.databinding.FragmentEditInfoBinding
import com.devsinc.tappze.model.AppIcon
import com.devsinc.tappze.ui.alert.AlertFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EditInfoFragment(private var appInfo: AppIcon) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentEditInfoBinding
    private val viewModel: EditInfoViewModel by viewModels()
    private lateinit var appUrls: MutableMap<String, String>

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentEditInfoBinding.bind(view)

        binding.ivAppLogo.setImageResource(appInfo.icon)
        binding.tvTitle.text = appInfo.name
        binding.tilName.hint = appInfo.name
        viewModel.getUserDatabaseInfo()

        lifecycleScope.launchWhenStarted {
            viewModel.getDatabaseInfoFlow.collect {
                when (it) {
                    is Resource.Success -> {
                        binding.etAppUrl.setText(it.result[appInfo.name])
                        appUrls = it.result
                    }
                    is Resource.Error -> {
                        val dialog = AlertFragment(it, "Error")
                        dialog.show(parentFragmentManager, AlertFragment.TAG)
                    }
                    is Resource.Loading -> {
                    }
                    else -> {}
                }
            }
        }

        binding.ivClose.setOnClickListener {
            dismiss()
        }

        binding.btnSave.setOnClickListener {
            if (binding.etAppUrl.text.toString() != "") {
                viewModel.updateUserDatabaseInfo(
                    binding.tvTitle.text.toString(),
                    binding.etAppUrl.text.toString()
                )
            } else {
                binding.etAppUrl.error = "Field cannot be empty"
                binding.etAppUrl.requestFocus()
                Constants.openKeyboard(binding.etAppUrl, requireContext())
            }
        }

        binding.btnDelete.setOnClickListener {
            viewModel.deleteUserDatabaseInfo(binding.tvTitle.text.toString())
        }

        binding.btnOpenUrl.setOnClickListener {
            val launchIntent: Intent? =
                this.context?.packageManager?.getLaunchIntentForPackage("com.google.android.${appInfo.name.lowercase()}")
            if (launchIntent != null) {
                launchIntent.putExtra(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.${appInfo.name.lowercase()}.com/${appUrls[appInfo.name]}")
                )
                startActivity(launchIntent)
            } else {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.${appInfo.name.lowercase()}.com/${appUrls[appInfo.name]}")
                )
                context?.startActivity(intent)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.deleteDatabaseInfoFlow.collect {
                when (it) {
                    is Resource.Success -> {
                        val dialog = AlertFragment(it, "Done")
                        dialog.show(parentFragmentManager, AlertFragment.TAG)
                        dismiss()
                    }
                    is Resource.Error -> {
                        val dialog = AlertFragment(it, "Error")
                        dialog.show(parentFragmentManager, AlertFragment.TAG)
                    }
                    is Resource.Loading -> {
                    }
                    else -> {}
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.updateDatabaseFlow.collect { status ->
                when (status) {
                    is Resource.Success -> {
                        val dialog = AlertFragment(status, "Done")
                        dialog.show(parentFragmentManager, AlertFragment.TAG)
                        dismiss()
                    }
                    is Resource.Error -> {
                        val dialog = AlertFragment(status, "Error")
                        dialog.show(parentFragmentManager, AlertFragment.TAG)
                        dismiss()
                    }
                    is Resource.Loading -> {
                    }
                    else -> {}
                }
            }
        }
    }
}