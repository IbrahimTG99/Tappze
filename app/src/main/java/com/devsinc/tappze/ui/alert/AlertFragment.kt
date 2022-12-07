package com.devsinc.tappze.ui.alert

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.devsinc.tappze.R
import com.devsinc.tappze.data.Resource
import com.devsinc.tappze.databinding.FragmentAlertBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlertFragment(private val alert: Resource<String>?, private val title: String) : DialogFragment() {

    private lateinit var binding: FragmentAlertBinding
    private val viewModel: AlertViewModel by viewModels()

    companion object {
        const val TAG = "AlertFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_alert, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAlertBinding.bind(view)

        binding.alertTitle.text = title

        if (alert is Resource.Success) {
            binding.alertMessage.text = alert.result.toString()
            binding.alertMessage.textSize = 12f
            binding.alertImage.setImageResource(R.drawable.ic_baseline_done_outline_24)
        } else if (alert is Resource.Error) {
            binding.alertMessage.text = alert.exception.message.toString()
            binding.alertMessage.textSize = 12f
            binding.alertImage.setImageResource(R.drawable.ic_baseline_error_outline_24)
        }

        binding.alertButton.setOnClickListener {
            dismiss()
        }
    }
}