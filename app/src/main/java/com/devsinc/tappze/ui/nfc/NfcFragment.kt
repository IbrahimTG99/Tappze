package com.devsinc.tappze.ui.nfc

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devsinc.tappze.R

class NfcFragment : Fragment() {

    companion object {
        fun newInstance() = NfcFragment()
    }

    private lateinit var viewModel: NfcViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nfc, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NfcViewModel::class.java)
        // TODO: Use the ViewModel
    }

}