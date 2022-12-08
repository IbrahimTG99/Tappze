package com.devsinc.tappze.ui.displayinfo

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
import com.devsinc.tappze.databinding.FragmentDisplayInfoBinding
import com.devsinc.tappze.model.AppIcon
import com.devsinc.tappze.ui.alert.AlertFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DisplayInfoFragment(private var appInfo: AppIcon?) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentDisplayInfoBinding
    private val viewModel: DisplayInfoViewModel by viewModels()
    private lateinit var appUrls: MutableMap<String, String>

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_display_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDisplayInfoBinding.bind(view)

        appInfo?.let { binding.ivAppLogo.setImageResource(it.icon) }
        binding.tvTitle.text = appInfo?.name ?: "No name"
        viewModel.getUserDatabaseInfo()

        lifecycleScope.launchWhenStarted {
            viewModel.getDatabaseInfoFlow.collect {
                when (it) {
                    is Resource.Success -> {
                        binding.tvAppUrl.text = it.result[appInfo?.name]
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

        binding.btnOpenUrl.setOnClickListener {
            val launchIntent: Intent? =
                this.context?.packageManager?.getLaunchIntentForPackage("com.google.android.${appInfo?.name?.lowercase()}")
            if (launchIntent != null) {
                launchIntent.putExtra(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.${appInfo?.name?.lowercase()}.com/${appUrls[appInfo?.name]}")
                )
                startActivity(launchIntent)
            } else {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.${appInfo?.name?.lowercase()}.com/${appUrls[appInfo?.name]}")
                )
                context?.startActivity(intent)
            }
        }
    }
}