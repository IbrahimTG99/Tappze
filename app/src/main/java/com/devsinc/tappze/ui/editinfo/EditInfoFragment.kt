package com.devsinc.tappze.ui.editinfo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.devsinc.tappze.R
import com.devsinc.tappze.data.Resource
import com.devsinc.tappze.databinding.FragmentEditInfoBinding
import com.devsinc.tappze.model.AppIcon
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditInfoFragment(private var appInfo: AppIcon) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentEditInfoBinding
    private val viewModel: EditInfoViewModel by viewModels()
    private lateinit var appUrls : MutableMap<String, String>

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
        binding.etAppUrl.hint = appInfo.name
        viewModel.getUserDatabaseInfo()

        lifecycleScope.launchWhenStarted {
            viewModel.getDatabaseInfoFlow.collect {
                when (it) {
                    is Resource.Success -> {
                        binding.etAppUrl.setText(it.result[appInfo.name])
                        appUrls = it.result
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.exception.message.toString(), Toast.LENGTH_SHORT).show()
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
            viewModel.updateUserDatabaseInfo(binding.tvTitle.text.toString(), binding.etAppUrl.text.toString())
        }

        binding.btnDelete.setOnClickListener {
            viewModel.deleteUserDatabaseInfo(binding.tvTitle.text.toString())
        }

        binding.btnOpenUrl.setOnClickListener {
            val launchIntent: Intent? =
                this.context?.packageManager?.getLaunchIntentForPackage("com.google.android.${appInfo.name.lowercase()}")
            if (launchIntent != null) {
                launchIntent.putExtra(Intent.ACTION_VIEW, Uri.parse("https://www.${appInfo.name.lowercase()}.com/${appUrls[appInfo.name]}"))
                startActivity(launchIntent)
            } else {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.${appInfo.name.lowercase()}.com/${appUrls[appInfo.name]}"))
                context?.startActivity(intent)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.deleteDatabaseInfoFlow.collect {
                when (it) {
                    is Resource.Success -> {
                        Toast.makeText(requireContext(), it.result, Toast.LENGTH_SHORT).show()
                        dismiss()
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.exception.message.toString(), Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(requireContext(), status.result.toString(), Toast.LENGTH_SHORT).show()
                        dismiss()
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), status.exception.toString(), Toast.LENGTH_SHORT).show()
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