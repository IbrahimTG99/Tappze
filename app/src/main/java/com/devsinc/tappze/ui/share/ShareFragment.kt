package com.devsinc.tappze.ui.share

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.devsinc.tappze.R
import com.devsinc.tappze.data.Resource
import com.devsinc.tappze.databinding.FragmentShareBinding
import com.devsinc.tappze.ui.BindingFragment
import com.devsinc.tappze.ui.alert.AlertFragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShareFragment : BindingFragment<FragmentShareBinding>() {

    companion object {
        fun newInstance() = ShareFragment()
    }

    private val viewModel: ShareViewModel by viewModels()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentShareBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUserDatabase()

        lifecycleScope.launchWhenStarted {
            viewModel.getDatabaseFlow.collect { event ->
                when (event) {
                    is Resource.Success -> {
                        val user = event.result
                        if (user.profileImage != null) {
                            Glide.with(this@ShareFragment)
                                .load(user.profileImage)
                                .placeholder(R.drawable.ic_baseline_account_circle_24)
                                .into(binding.profileImage)
                        }
                    }
                    is Resource.Error -> {
                        val dialog = AlertFragment(event, "Error Loading User Data")
                        dialog.show(parentFragmentManager, AlertFragment.TAG)
                    }
                    else -> {}
                }
            }
        }
        // generate qr code and store in image view
        val bitmap = getQrCodeBitmap(binding.tvLink.text.toString(), "")
        binding.ivQrCode.setImageBitmap(bitmap)
    }

    private fun getQrCodeBitmap(ssid: String, password: String): Bitmap {
        val size = 512 //pixels
        val qrCodeContent = "WIFI:S:$ssid;T:WPA;P:$password;;"
        val hints = hashMapOf<EncodeHintType, Int>().also {
            it[EncodeHintType.MARGIN] = 1
        } // Make the QR code buffer border narrower
        val bits = QRCodeWriter().encode(qrCodeContent, BarcodeFormat.QR_CODE, size, size)
        return Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565).also {
            for (x in 0 until size) {
                for (y in 0 until size) {
                    it.setPixel(
                        x,
                        y,
                        if (bits[x, y]) Color.BLACK else ContextCompat.getColor(
                            requireContext(),
                            R.color.accent
                        )
                    )
                }
            }
        }
    }
}